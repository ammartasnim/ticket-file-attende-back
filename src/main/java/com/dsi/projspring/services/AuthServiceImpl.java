package com.dsi.projspring.services;

import com.dsi.projspring.dtos.*;
import com.dsi.projspring.entities.*;
import com.dsi.projspring.repositories.*;
import com.dsi.projspring.utils.JwtUtils;
import com.dsi.projspring.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AgencyRepository agencyRepository;
    private final AgentRepository agentRepository;
    private final CounterRepository counterRepository;
    private final TicketRepository ticketRepository;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, AgencyRepository agencyRepository, AgentRepository agentRepository, CounterRepository counterRepository,  TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.agencyRepository = agencyRepository;
        this.agentRepository = agentRepository;
        this.ticketRepository = ticketRepository;
        this.counterRepository = counterRepository;
    }

    @Override
    public UserResponseDTO login(LoginRequest request) {
        if (request == null) {
            throw new RuntimeException("No authentication found");}
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("User not found"));

        String token= jwtUtils.generateToken(user);

        UserResponseDTO.UserResponseDTOBuilder builder = UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token);
        if (user instanceof Agent agent) {
            builder.agencyId(agent.getAgency().getId())
                    .agencyName(agent.getAgency().getName())
                    .counterNumber(agent.getCounter() != null ? agent.getCounter().getNumber() : null);
        }
        return builder.build();
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }
        Client client  = new Client();
        client.setEmail(request.getEmail());
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setActive(true);
        client.setRole(Role.CLIENT);
        userRepository.save(client);
    }

    @Override
    @Transactional
    public void registerAgent(RegisterRequest request, Long agencyId) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");}
        Agency agency = agencyRepository.findById(agencyId).orElseThrow(() -> new RuntimeException("Agency not found"));
        Agent agent = new Agent();
        agent.setFirstName(request.getFirstName());
        agent.setLastName(request.getLastName());
        agent.setEmail(request.getEmail());
        agent.setPhoneNumber(request.getPhoneNumber());
        agent.setPassword(passwordEncoder.encode(request.getPassword()));
        agent.setRole(Role.AGENT);
        agent.setAgency(agency);
        agent.setActive(true);
        userRepository.save(agent);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user instanceof Agent agent) {
            if (agent.getCounter() != null) {
                Counter counter = agent.getCounter();
                counter.setAgent(null);
                counter.setActive(false);
                counterRepository.save(counter);
            }
        }
        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDTO> getAgentsByAgency(Long agencyId) {
        List<Agent> agents = agentRepository.findByAgency(agencyId);
        List<UserResponseDTO> dtos = new ArrayList<>();
        for (Agent agent : agents) {
            dtos.add(userMapper.toDto(agent));
        }
        return dtos;
    }

    @Override
    public List<UserResponseDTO> getAllAgents() {
        List<Agent> agents = agentRepository.findAll();
        List<UserResponseDTO> dtos = new ArrayList<>();
        for (Agent agent : agents) {
            dtos.add(userMapper.toDto(agent));
        }
        return dtos;
    }

    @Override
    public UserResponseDTO getMe(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("The current password you entered is incorrect");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old one");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end   = LocalDate.now().atTime(LocalTime.MAX);
        long totalAgencies  = agencyRepository.count();
        long totalAgents    = agentRepository.count();
        long todayTickets   = ticketRepository.countByAppointmentDateBetween(start, end);
        long waitingTickets = ticketRepository.countByStatus(Status.GENERATED);
        long activeCounters = counterRepository.countByIsActiveTrue();
        return new DashboardStatsDTO(
                totalAgencies,
                totalAgents,
                todayTickets,
                waitingTickets,
                activeCounters
        );
    }
}

package devrep.projet.devmed.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import devrep.projet.devmed.entities.Utilisateur;
import devrep.projet.devmed.repository.UtilisateurRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) 
        throws UsernameNotFoundException {
        Utilisateur user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new MyUserDetails(user);
    }
    
}

package com.shaker.shakerecommerce.shared;

import com.shaker.shakerecommerce.exceptions.BusinessException;
import com.shaker.shakerecommerce.exceptions.ResourceNotFoundException;
import com.shaker.shakerecommerce.model.User;
import com.shaker.shakerecommerce.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepo userRepo;

   public User getLoggedInUser() throws ResourceNotFoundException {
       String email = getLoggedInUserEmail();
       return userRepo.findByEmailOrUsername(email, email)
               .orElseThrow(() -> new ResourceNotFoundException("User", "email/usenaame", email));
   }



    public String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new BusinessException("Unauthenticated user");
        }
        return userDetails.getUsername();
    }

    public Long getLoggedInUserId() throws ResourceNotFoundException {
        String email = getLoggedInUserEmail();
        User user =  userRepo.findByEmailOrUsername(email, email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email/usenaame", email));

        return user.getId();
    }

}

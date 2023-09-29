package com.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.mail.Multipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.entity.Contact;
import com.entity.User;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;
import com.repository.ContactRepository;
import com.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class ContactController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder brEncoder;
	
	@Autowired
	ContactRepository contactRepository;
	
	//comman method for contact user controller
	@ModelAttribute
	public void commanMethod(Principal principal,Model model) {
		
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		model.addAttribute("user", user);
		
		System.out.println("USER: "+user);
		
	}
	
	    // home dahsboard
		@GetMapping("/index")
		public String dashBoard(Model model, Principal principal) {
       String name = principal.getName();
       User user = userRepository.getUserByUserName(name);
	     System.out.println(user.toString());
	     System.out.println(user.getName());
	     model.addAttribute("user", user);
			model.addAttribute("title", "User Dashboard");
			return "User/dashboard";
		}
		
		//update password handler
		@GetMapping("/update-password")
        public String updatePassword() {
        	
        	return "User/update_password";
        }
		
		
		//update password user
		@PostMapping("/update_user_password")
	    public String updatePassworduser(@RequestParam("oldpassword") String oldpassword,@RequestParam("newpassword") String newpassword, Principal principal, HttpSession session ) {
	    	
			User user = userRepository.getUserByUserName(principal.getName());
			
			if(brEncoder.matches(oldpassword,user.getPassword())){
				
				//chnage password
				user.setPassword(brEncoder.encode(newpassword));
				
				userRepository.save(user);
				
				System.out.println("password update successfully");		
				
				return "redirect:/user/index";
		}
			return "redirect:/user/index";
           
	    }
		
		
		//add contact handler
		@GetMapping("/add-contact")
		public String addContact(){
			
			return "User/add_contact";
		}
		
		//add contact process handler
		@PostMapping("/insert-contact")
		public String addContactHandler(@Valid @ModelAttribute Contact contact,BindingResult bindingResult,@RequestParam("cimage") MultipartFile file, Model model, Principal principal,HttpSession session ) throws Throwable {
			
			    User user = userRepository.getUserByUserName(principal.getName());
			   
			   if(!file.isEmpty()) {
				   
				   File newFile = new ClassPathResource("static/img").getFile();
			   
				      Path path = Paths.get(newFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				      
				      Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				      
				      System.out.println("image uploaded");
				         contact.setCimage(file.getOriginalFilename());
				         
			   }
			   else {
			         contact.setCimage("contact.png");
			   }
				        contact.setUser(user);
				        
				        contactRepository.save(contact);
				        
				        user.getContacts().add(contact);
				         
				        userRepository.save(user);
				        System.out.println("contact added");
			   
			
			return "User/add_contact";
		}

		@GetMapping("/show-contact/{page}")
		public String showContacts(@PathVariable("page") Integer page,Principal principal,HttpSession session,Model model) {
			
			User user = userRepository.getUserByUserName(principal.getName());
			
			//currentPage-page 1
			  //contact per page -7
			   Pageable pageable = PageRequest.of(page, 5);
			   
			   //contact finding by userid;
			   Page<Contact> contacts = contactRepository.findContactsByUser(user.getId(),pageable);
			   
			      model.addAttribute("contacts", contacts);
			      model.addAttribute("currentPage", page);
			      model.addAttribute("totalPages", contacts.getTotalPages());
			   
		
			  return "User/show_contact";
			
		}
		
		//edit contact handler
		@PostMapping("/edit_user/{id}")
		public String editContact(@PathVariable("id") int id,Principal principal,Model model,HttpSession session) {
			System.out.println("======================");
			
			   Contact contact = contactRepository.findById(id).get();
			
			   System.out.println("conatct "+contact.toString());
			   
			   model.addAttribute("contact", contact);
			
			   return "User/edit_user";
			
		}
		
		//update contact handler submit
		@PostMapping("/update-contact")
		public String updateContact(@Valid @ModelAttribute Contact contact,BindingResult result,@RequestParam("cimage") MultipartFile file,Principal principal,HttpSession session,Model model) {
			
			System.out.println("contact in update: "+contact.toString());
			
			System.out.println("Amrit");
			
			User user = userRepository.getUserByUserName(principal.getName());
			
			   try {
				   Contact oldcontact = contactRepository.findById(contact.getCid()).get();
				
				   if(!file.isEmpty()) {
					   
					   File deletefile = new ClassPathResource("/static/img/").getFile();
	
					   File file2 = new File(deletefile, oldcontact.getCimage());
					   
					   file2.delete();
					   
					   //rewrite image new image upload
					   
					        File newfile = new ClassPathResource("static/img").getFile();
					   
					        Path path = Paths.get(newfile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					   
					        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					     
					         
					        contact.setCimage(file.getOriginalFilename());
					        
					        
					        System.out.println("new profile pic updated");
				   }
				   else {
					   contact.setCimage(oldcontact.getCimage());
				   }
				   
				   contact.setUser(user);
				   
				   contactRepository.save(contact);
					return "redirect:/user/show-contact";
				   
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return "redirect:/user/show-contact/0";
		}
		
		//delete contact
		@GetMapping("/delete/{id}")
		public String deleteContact(@PathVariable("id") int cid,HttpSession session,
				                            Principal principal,Model model){
			
			System.out.println("inside delete controller");
			  Contact contact = contactRepository.findById(cid).get();
			
			  User user = userRepository.getUserByUserName(principal.getName());
			  
			  if(user.getId()==contact.getUser().getId()) {
				  
				  contactRepository.delete(contact);
				   
				  user.getContacts().remove(contact);
				  
				  userRepository.save(user);
				  
			  }
  
			  System.out.println("contact deleted successfully");
			  
			return "redirect:/user/show-contact/0";
		}
		
		//user profile
		@GetMapping("/profile")
		public String userProfile(Model m,Principal principal){
			
			User u = userRepository.getUserByUserName(principal.getName());
			
			   m.addAttribute("u",u);
			
			m.addAttribute("title", "user profile");
			
			return "User/profile";	
		}
		
		//contact details
		@GetMapping("/contact/{cid}")
		public String getContactDetails(@PathVariable("cid") Integer cid,Principal principal,Model model) {
			
			    Contact contact = contactRepository.findById(cid).get();
			    
			    System.out.println("contact detail: "+contact);
			   model.addAttribute("contact", contact);
			   
			return "User/contact_detail";
		}
		
		
		
}

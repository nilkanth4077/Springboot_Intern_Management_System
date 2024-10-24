package com.rh4.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.rh4.entities.*;
import com.rh4.models.ReportFilter;
import com.rh4.repositories.AdminRepo;
import com.rh4.repositories.CancelledRepo;
import com.rh4.repositories.GroupRepo;
import com.rh4.repositories.InternApplicationRepo;
import com.rh4.repositories.InternRepo;
import com.rh4.repositories.UserRepo;
import com.rh4.services.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Path;

@Controller
@RequestMapping("/bisag/admin")
public class AdminController {

    @Autowired
    HttpSession session;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private InternService internService;
    @Autowired
    private MyUserService myUserService;
    @Autowired
    private InternApplicationRepo internApplicationRepo;
    @Autowired
    private EmailSenderService emailService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private WeeklyReportService weeklyReportService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private CancelledRepo cancelledRepo;
    @Autowired
    private InternRepo internRepo;
    @Autowired
    private GuideService guideService;
    @Autowired
    private DataExportService dataExportService;
    ;

    @org.springframework.beans.factory.annotation.Value("${icard.filepath}")
    private String icardfolderpath;
    @org.springframework.beans.factory.annotation.Value("${noc.filepath}")
    private String nocfolderpath;
    @org.springframework.beans.factory.annotation.Value("${resume.filepath}")
    private String resumefolderpath;
    @org.springframework.beans.factory.annotation.Value("${psimage.filepath}")
    private String psimagefolderpath;
    @org.springframework.beans.factory.annotation.Value("${icardForm.filepath}")
    private String icardForm;
    @org.springframework.beans.factory.annotation.Value("${registrationForm.filepath}")
    private String registrationForm;
    @org.springframework.beans.factory.annotation.Value("${securityForm.filepath}")
    private String securityForm;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Admin getSignedInAdmin() {
        String username = (String) session.getAttribute("username");
        Admin admin = adminService.getAdminByUsername(username);
        return admin;
    }
    // generate internid//////////////////////////////////////

    public String generateInternId() {
        // Generate custom internId using current year and serial number
        SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
        String currentYear = yearFormat.format(new Date());

        // Assuming you have a method to get the next serial number
        int serialNumber = generateSerialNumber();
        ++serialNumber;
        // Combine the parts to form the custom internId
        String sno = String.valueOf(serialNumber);
        String formattedSerialNumber = String.format("%04d", Integer.parseInt(sno));
        System.out.println("serialNumber..." + serialNumber);
        System.out.println("formated..." + formattedSerialNumber);
        String internId = currentYear + "BISAG" + formattedSerialNumber;
        return internId;
    }

    public int generateSerialNumber() {

        String id = internService.getMostRecentInternId();
        if (id == null)
            return 0;
        String serialNumber = id.substring(id.length() - 4);
        int lastFourDigits = Integer.parseInt(serialNumber);
        return lastFourDigits;
    }

    // generate groupid////////////////////////////////////////////////////////////

//	public String generateGroupId() {
//		// Generate custom groupId using current year and serial number
//		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
//		String currentYear = yearFormat.format(new Date());
//
//		// Assuming you have a method to get the next serial number
//		int serialNumber = generateSerialNumberForGroup();
//		++serialNumber;
//		// Combine the parts to form the custom internId
//		String sno = String.valueOf(serialNumber);
//		String formattedSerialNumber = String.format("%03d", Integer.parseInt(sno));
//		System.out.println("serialNumber..." + serialNumber);
//		System.out.println("formated..." + formattedSerialNumber);
//		String groupId = currentYear + "G" + formattedSerialNumber;
//		return groupId;
//	}
//
//	public int generateSerialNumberForGroup() {
//
//		String id = groupService.getMostRecentGroupId();
//		if (id == null)
//			return 0;
//		String serialNumber = id.substring(id.length() - 3);
//		int lastThreeDigits = Integer.parseInt(serialNumber);
//		return lastThreeDigits;
//	}

    // Method to generate the group ID
    public String generateGroupId() {
        // Check if the current year has changed
        int year = getCurrentYear();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String currentYear = yearFormat.format(new Date());
        int currentYearInt = Integer.parseInt(currentYear);
        int serialNumber = generateSerialNumberForGroup();
        if (year != currentYearInt) {
            currentYearInt = year; // Update the current year
            serialNumber = 0;   // Reset the serial number to 0
        }

        // Increment the serial number
        serialNumber++;

        // Format the serial number
        String formattedSerialNumber = String.format("%03d", serialNumber);

        // Combine the parts to form the custom group ID
        String groupId = currentYear + "G" + formattedSerialNumber;

        return groupId;
    }

    // Method to get the current year
    private int getCurrentYear() {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String yearString = yearFormat.format(new Date());
        return Integer.parseInt(yearString);
    }

    // Method to generate the serial number for group
    private int generateSerialNumberForGroup() {
        String id = groupService.getMostRecentGroupId();
        if (id == null || id.isEmpty()) {
            return 0;
        } else {
            // Extract the serial number part from the group ID
            String serialNumberString = id.substring(5); // Assuming "yyyyG" prefix
            return Integer.parseInt(serialNumberString);
        }
    }

    Model countNotifications(Model model) {
        // count

        //count approve for interview
        long approveForInterviewApplicationsCount = internService.approveForInterviewApplicationsCount();
        model.addAttribute("interviewPendingApplicationsCount", approveForInterviewApplicationsCount);

        //count approve for internship(get interview pending results )
        long countPendingInterviewApplications = internService.countPendingInterviewApplications();
        model.addAttribute("countInterviewApplications", countPendingInterviewApplications);

        //Project definitions remaining to approve from admin
        long adminPendingProjectDefinitionCount = groupService.adminPendingProjectDefinitionCount();
        model.addAttribute("adminPendingProjectDefinitionCount", adminPendingProjectDefinitionCount);

        long pendingGroupCreationCount = internService.pendingGroupCreationCount();
        model.addAttribute("pendingGroupCreationCount", pendingGroupCreationCount);

        //cancellation request count
        long requestedCancellationsCount = internService.countRequestedCancellations();
        model.addAttribute("requestedCancellationsCount", requestedCancellationsCount);

        long countGuide = guideService.countGuides();
        model.addAttribute("countGuide", countGuide);

        return model;
    }
    // Admin Dashboard

    @GetMapping("/admin_dashboard")
    public ModelAndView admin_dashboar(Model model) {

        ModelAndView mv = new ModelAndView("admin/admin_dashboard");

        // Retrieve the username from the session
        String username = (String) session.getAttribute("username");

        // Use the adminService to get the Admin object based on the username
        Admin admin = adminService.getAdminByUsername(username);

        // Set the "id" and "username" attributes in the session
        session.setAttribute("id", admin.getAdminId());
        session.setAttribute("username", username);

        model = countNotifications(model);

        long countInterns = internService.countInterns();
        model.addAttribute("countInterns", countInterns);
        // Add the username to the ModelAndView
        mv.addObject("username", username);
        mv.addObject("admin", admin);

        return mv;
    }

    // Group Manage

    // Intern Management///////////////////////////////////////////////

    @GetMapping("/register_intern")
    public String registerIntern() {
        return "admin/InternRegistration";
    }

    @PostMapping("/register_intern")
    public String registerIntern(@ModelAttribute("intern") Intern intern) {
        intern.setInternId(generateInternId());
        internService.registerIntern(intern);
        return "redirect:/bisag";
    }

    @GetMapping("/intern/{id}")
    public ModelAndView internDetails(@PathVariable("id") String id, Model model) {
        ModelAndView mv = new ModelAndView();
        Optional<Intern> intern = internService.getIntern(id);
        model = countNotifications(model);
        mv.addObject("intern", intern);
        List<College> colleges = fieldService.getColleges();
        List<Domain> domains = fieldService.getDomains();
        List<Branch> branches = fieldService.getBranches();
        List<GroupEntity> groups = groupService.getGroups();
        mv.addObject("colleges", colleges);
        mv.addObject("domains", domains);
        mv.addObject("branches", branches);
        mv.addObject("groups", groups);
        mv.setViewName("admin/intern_detail");
        return mv;
    }

    @GetMapping("/update_admin/{id}")
    public ModelAndView updateAdmin(@PathVariable("id") long id, Model model) {
        ModelAndView mv = new ModelAndView("super_admin/update_admin");
        Optional<Admin> admin = adminService.getAdmin(id);
        mv.addObject("admin", admin.orElse(new Admin()));
        model = countNotifications(model);
        return mv;
    }

    @PostMapping("/update_admin/{id}")
    public String updateAdmin(@ModelAttribute("admin") Admin admin, @PathVariable("id") long id) {
        Optional<Admin> existingAdmin = adminService.getAdmin(admin.getAdminId());

        if (existingAdmin.isPresent()) {

            String currentPassword = existingAdmin.get().getPassword();
            Admin updatedAdmin = existingAdmin.get();
            updatedAdmin.setName(admin.getName());
            updatedAdmin.setLocation(admin.getLocation());
            updatedAdmin.setContactNo(admin.getContactNo());
            updatedAdmin.setEmailId(admin.getEmailId());

            // Save the updated admin entity
            adminService.updateAdmin(updatedAdmin, existingAdmin);
        }
        return "redirect:/logout";
    }

    // Manage intern application///////////////////////////////////
    @GetMapping("/intern_application")
    public ModelAndView internApplication(Model model) {
        ModelAndView mv = new ModelAndView("admin/intern_application");
        List<InternApplication> interns = internService.getInternApplication();
        model = countNotifications(model);
        mv.addObject("interns", interns);
        return mv;
    }

    @GetMapping("/intern_application/{id}")
    public ModelAndView internApplication(@PathVariable("id") long id, Model model) {
        System.out.println("id" + id);
        ModelAndView mv = new ModelAndView();
        Optional<InternApplication> intern = internService.getInternApplication(id);
        mv.addObject("intern", intern);
        List<College> colleges = fieldService.getColleges();
        List<Domain> domains = fieldService.getDomains();
        List<Branch> branches = fieldService.getBranches();
        model = countNotifications(model);
        mv.addObject("colleges", colleges);
        mv.addObject("domains", domains);
        mv.addObject("branches", branches);
        mv.setViewName("admin/intern_application_detail");
        return mv;
    }

    @GetMapping("/intern_application_docs/{id}")
    public ModelAndView internApplicationDocs(@PathVariable("id") long id, Model model) {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id); // Implement this service method
        System.out.println("ID: " + id);
        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get(); // Retrieve the InternApplication object
            model.addAttribute("id", id);
            model.addAttribute("passportSizeImage", application.getPassportSizeImage());
            model.addAttribute("collegeIcardImage", application.getCollegeIcardImage());
            model.addAttribute("nocPdf", application.getNocPdf());
            model.addAttribute("resumePdf", application.getResumePdf());
        } else {
            model.addAttribute("error", "Intern Application not found");
        }

        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/intern_application_docs");
        return mv;
    }

    @GetMapping("/documents/passport/{id}")
    public ResponseEntity<byte[]> getPassportSizeImage(@PathVariable("id") long id) {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);

        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();

            byte[] image = application.getPassportSizeImage();

            if (image != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(image);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/documents/icard/{id}")
    public ResponseEntity<byte[]> getICardImage(@PathVariable("id") long id) {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);

        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();

            byte[] image = application.getCollegeIcardImage();

            if (image != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(image);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/documents/noc/{id}")
    public ResponseEntity<byte[]> getNocPdf(@PathVariable("id") long id) {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);

        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();

            byte[] pdf = application.getNocPdf();

            if (pdf != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdf);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/documents/resume/{id}")
    public ResponseEntity<byte[]> getResumePdf(@PathVariable("id") long id) {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);

        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();

            byte[] pdf = application.getResumePdf();

            if (pdf != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdf);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/documents/passport/{id}")
    public String updatePassportSizeImage(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);
        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();
            application.setPassportSizeImage(file.getBytes());
            internService.save(application);
            return "redirect:/bisag/admin/intern_application_docs/" + id;
        }
        return "redirect:/bisag/admin/intern_application_docs/" + id;
    }

    @PostMapping("/documents/icard/{id}")
    public String updateICardImage(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);
        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();
            application.setCollegeIcardImage(file.getBytes());
            internService.save(application);
            return "redirect:/bisag/admin/intern_application_docs/" + id;
        }
        return "redirect:/bisag/admin/intern_application_docs/" + id;
    }

    @PostMapping("/documents/noc/{id}")
    public String updateNocPdf(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);
        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();
            application.setNocPdf(file.getBytes());
            internService.save(application);
            return "redirect:/bisag/admin/intern_application_docs/" + id;
        }
        return "redirect:/bisag/admin/intern_application_docs/" + id;
    }

    @PostMapping("/documents/resume/{id}")
    public String updateResumePdf(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<InternApplication> optionalApplication = internService.getInternApplication(id);
        if (optionalApplication.isPresent()) {
            InternApplication application = optionalApplication.get();
            application.setResumePdf(file.getBytes());
            internService.save(application);
            return "redirect:/bisag/admin/intern_application_docs/" + id;
        }
        return "redirect:/bisag/admin/intern_application_docs/" + id;
    }

    @PostMapping("/intern_application/ans")
    public String internApplicationSubmission(@RequestParam String message, @RequestParam long id,
                                              @RequestParam String status, @RequestParam String finalStatus) {
        System.out.println("id: " + id + ", status: " + status);
        // Long ID = Long.parseLong(id);
        Optional<InternApplication> intern = internService.getInternApplication(id);
        intern.get().setStatus(status);
        intern.get().setFinalStatus(finalStatus);
        internService.addInternApplication(intern.get());
        if (status.equals("rejected")) {
            emailService.sendSimpleEmail(intern.get().getEmail(),
                    "Notification: Rejection of BISAG Internship Application\r\n" + "\r\n" + "Dear "
                            + intern.get().getFirstName() + ",\r\n" + "\r\n"
                            + "We appreciate your interest in the BISAG internship program and the effort you put into your application. After careful consideration, we regret to inform you that your application has not been successful on this occasion.\r\n"
                            + "\r\n"
                            + "Please know that the decision was a difficult one, and we had many qualified candidates. We want to thank you for your interest in joining our team and for taking the time to apply for the internship position.\r\n"
                            + "\r\n"
                            + "We encourage you to continue pursuing your goals, and we wish you the best in your future endeavors. If you have any feedback or questions about the decision, you may reach out to [Contact Person/Department].\r\n"
                            + "\r\n"
                            + "Thank you again for considering BISAG for your internship opportunity. We appreciate your understanding.\r\n"
                            + "\r\n" + "Best regards,\r\n" + "\r\n" + "Your Colleague,\r\n"
                            + "Internship Coordinator\r\n" + "BISAG INTERNSHIP PROGRAM\r\n" + "1231231231",
                    "BISAG INTERNSHIP RESULT");
        } else
            emailService.sendSimpleEmail(intern.get().getEmail(), message + "your unique id is " + intern.get().getId(),
                    "BISAG INTERNSHIP RESULT");
        return "redirect:/bisag/admin/intern_application_detail/" + id;
    }


    @PostMapping("/intern_application/update")
    public String internApplicationSubmission(@RequestParam long id, InternApplication internApplication, MultipartHttpServletRequest req) throws IllegalStateException, IOException, Exception {
        Optional<InternApplication> intern = internService.getInternApplication(id);

        if (internApplication.getIsActive() == true) {
            intern.get().setFirstName(internApplication.getFirstName());
            intern.get().setLastName(internApplication.getLastName());
            intern.get().setContactNo(internApplication.getContactNo());

            MyUser user = myUserService.getUserByUsername(intern.get().getEmail());
            user.setUsername(internApplication.getEmail());
            userRepo.save(user);

            intern.get().setEmail(internApplication.getEmail());
            intern.get().setCollegeName(internApplication.getCollegeName());
            intern.get().setIsActive(true);
            intern.get().setBranch(internApplication.getBranch());
            intern.get().setDomain(internApplication.getDomain());
            intern.get().setSemester(internApplication.getSemester());
            intern.get().setJoiningDate(internApplication.getJoiningDate());
            intern.get().setCompletionDate(internApplication.getCompletionDate());
        } else {
            intern.get().setIsActive(false);
            Cancelled cancelledEntry = new Cancelled();
            cancelledEntry.setTableName("InternApplication");
            cancelledEntry.setCancelId(Long.toString(intern.get().getId()));
            cancelledRepo.save(cancelledEntry);
        }

        internService.addInternApplication(intern.get());
        return "redirect:/bisag/admin/intern_application/" + id;
    }


    @PostMapping("/intern/update")
    public String updateIntern(@RequestParam String id, Intern internApplication, @RequestParam("groupId") String groupId, MultipartHttpServletRequest req) throws IllegalStateException, IOException, Exception {
        Optional<Intern> intern = internService.getIntern(id);


        if (groupId.equals("createOwnGroup")) {
            String generatedId = generateGroupId();
            GroupEntity group = new GroupEntity();
            group.setGroupId(generatedId);
            groupService.registerGroup(group);
            intern.get().setGroup(group);
        } else {
            intern.get().setGroup(groupService.getGroup(groupId));
        }
        if (intern.get().getIsActive()) {
            intern.get().setFirstName(internApplication.getFirstName());
            intern.get().setLastName(internApplication.getLastName());
            intern.get().setContactNo(internApplication.getContactNo());

            MyUser user = myUserService.getUserByUsername(intern.get().getEmail());
            user.setUsername(internApplication.getEmail());
            userRepo.save(user);

            InternApplication internA = internService.getInternApplicationByUsername(intern.get().getEmail());
            internA.setEmail(internApplication.getEmail());
            internApplicationRepo.save(internA);

            intern.get().setEmail(internApplication.getEmail());
            intern.get().setCollegeName(internApplication.getCollegeName());
            intern.get().setBranch(internApplication.getBranch());
            intern.get().setIsActive(true);
            intern.get().setDomain(internApplication.getDomain());
            intern.get().setSemester(internApplication.getSemester());
            intern.get().setJoiningDate(internApplication.getJoiningDate());
            intern.get().setCompletionDate(internApplication.getCompletionDate());
            intern.get().setPermanentAddress(internApplication.getPermanentAddress());
            intern.get().setDateOfBirth(internApplication.getDateOfBirth());
            intern.get().setGender(internApplication.getGender());
            intern.get().setCollegeGuideHodName(internApplication.getCollegeGuideHodName());
            intern.get().setDegree(internApplication.getDegree());
            intern.get().setAggregatePercentage(internApplication.getAggregatePercentage());
            intern.get().setUsedResource(internApplication.getUsedResource());

//			if(req.getFile("icardImageone")!=null)
//			{
//				intern.get().setIcardImage(uploadfile(req.getFile("icardImageone"),"icard",intern.get().getInternId()));
//				
//			}
//			if(req.getFile("nocPdfone")!=null)
//			{
//				intern.get().setNocPdf(uploadfile(req.getFile("nocPdfone"),"noc",intern.get().getInternId()));
//			}
//			if(req.getFile("resumePdfone")!=null)
//			{
//				intern.get().setResumePdf(uploadfile(req.getFile("resumePdfone"),"resume",intern.get().getInternId()));
//			}
//			if(req.getFile("passportSizeImageone")!=null)
//			{
//				intern.get().setPassportSizeImage(uploadfile(req.getFile("passportSizeImageone"),"psimage",intern.get().getInternId()));
//			}
//			if(req.getFile("")!=null)
//			{
//				intern.get().getSecurityForm(uploadfile(req.getFile("securityForm"),"securityForm",intern.get().getInternId()));
//			}
//			if(req.getFile("passportSizeImageone")!=null)
//			{
//				intern.get().setRegistrationForm(uploadfile(req.getFile("registrationForm"),"registrationForm",intern.get().getInternId()));
//			}
//			if(req.getFile("passportSizeImageone")!=null)
//			{
//				intern.get().setIcardForm(uploadfile(req.getFile("icardForm"),"icardForm",intern.get().getInternId()));
//			}
        }


        if (internApplication.getIsActive() == false) {
            intern.get().setIsActive(false);
            intern.get().setCancellationStatus("cancelled");
            Cancelled cancelledEntry = new Cancelled();
            cancelledEntry.setTableName("intern");
            cancelledEntry.setCancelId(intern.get().getInternId());
            cancelledRepo.save(cancelledEntry);
        }
        internRepo.save(intern.get());
        return "redirect:/bisag/admin/intern_application/new_interns";
    }


    @GetMapping("/intern_application/approved_interns")
    public ModelAndView approvedInterns(Model model) {
        ModelAndView mv = new ModelAndView();
        List<InternApplication> intern = internService.getApprovedInterns();
        model = countNotifications(model);
        mv.addObject("interns", intern);
        mv.setViewName("admin/approved_interns");
        return mv;
    }

    @GetMapping("/intern_application/approved_intern/{id}")
    public ModelAndView approvedInterns(@PathVariable("id") long id, Model model) {
        System.out.println("approved id" + id);
        ModelAndView mv = new ModelAndView();
        Optional<InternApplication> intern = internService.getInternApplication(id);
        mv.addObject("intern", intern);
        List<College> colleges = fieldService.getColleges();
        List<Domain> domains = fieldService.getDomains();
        List<Branch> branches = fieldService.getBranches();
        model = countNotifications(model);
        mv.addObject("colleges", colleges);
        mv.addObject("domains", domains);
        mv.addObject("branches", branches);
        mv.setViewName("admin/approved_intern_application_detail");
        return mv;
    }

    @PostMapping("/intern_application/approved_intern/update")
    public String approvedInterns(@RequestParam long id, InternApplication internApplication, MultipartHttpServletRequest req) throws IllegalStateException, IOException, Exception {
        Optional<InternApplication> intern = internService.getInternApplication(id);

        if (internApplication.getIsActive() == true) {
            intern.get().setFirstName(internApplication.getFirstName());
            intern.get().setLastName(internApplication.getLastName());
            intern.get().setContactNo(internApplication.getContactNo());
            intern.get().setEmail(internApplication.getEmail());
            intern.get().setIsActive(true);
            intern.get().setCollegeName(internApplication.getCollegeName());
            intern.get().setBranch(internApplication.getBranch());
            intern.get().setDomain(internApplication.getDomain());
            intern.get().setSemester(internApplication.getSemester());
            intern.get().setJoiningDate(internApplication.getJoiningDate());
            intern.get().setCompletionDate(internApplication.getCompletionDate());
        } else {
            intern.get().setIsActive(false);
            Cancelled cancelledEntry = new Cancelled();
            cancelledEntry.setTableName("InternApplication");
            cancelledEntry.setCancelId(Long.toString(intern.get().getId()));
            cancelledRepo.save(cancelledEntry);
        }

        internService.addInternApplication(intern.get());
        return "redirect:/bisag/admin/intern_application/approved_interns";
    }

    @PostMapping("/intern_application/approved_intern/ans")
    public String approvedInterns(@RequestParam String message, @RequestParam long id,
                                  @RequestParam String finalStatus) {
        System.out.println("iddd" + id + finalStatus);
        // Long ID = Long.parseLong(id);
        Optional<InternApplication> intern = internService.getInternApplication(id);
        intern.get().setFinalStatus(finalStatus);
        internService.addInternApplication(intern.get());

        if (finalStatus.equals("failed")) {
            emailService.sendSimpleEmail(intern.get().getEmail(), "You are Failed", "BISAG INTERNSHIP RESULT");
        } else {
            String finalmessage = message + "\n" + "username: " + intern.get().getFirstName()
                    + intern.get().getLastName() + "\n Password: " + intern.get().getFirstName() + "_"
                    + intern.get().getId();
            emailService.sendSimpleEmail(intern.get().getEmail(), finalmessage, "BISAG INTERNSHIP RESULT");
        }
        return "redirect:/bisag/admin/intern_application_detail/" + id;
    }

    @GetMapping("/intern_application/new_interns")
    public ModelAndView newInterns(Model model) {
        ModelAndView mv = new ModelAndView();
        List<Intern> intern = internService.getInterns();
        mv.addObject("intern", intern);
        model = countNotifications(model);
        mv.setViewName("admin/new_interns");
        return mv;
    }

    // Group Creation//////////////////////////////////////////

    @GetMapping("/create_group")
    public ModelAndView groupCreation(Model model) {
        ModelAndView mv = new ModelAndView();
        List<InternApplication> intern = internService.getInternApplication();
        mv.addObject("interns", intern);
        model = countNotifications(model);
        mv.setViewName("admin/create_group");
        return mv;
    }

    @PostMapping("/create_group_details")
    public String createGroup(@RequestParam("selectedInterns") List<Long> selectedInterns) {

        System.out.println("Selected Intern IDs: " + selectedInterns);
        // generate group id
        String id = generateGroupId();
        GroupEntity group = new GroupEntity();
        Optional<InternApplication> internoptional = internService.getInternApplication(selectedInterns.get(0));
        group.setDomain(internoptional.get().getDomain());
        group.setGroupId(id);
        groupService.registerGroup(group);

        // register those intern
        for (Long internId : selectedInterns) {
            Optional<InternApplication> internApplicationOptional = internService.getInternApplication(internId);

            if (internApplicationOptional.isPresent()) {
                InternApplication internApplication = internApplicationOptional.get();
                internApplication.setGroupCreated(true);
                internService.addInternApplication(internApplication);

                // Create an Intern object using a constructor or a factory method
                Intern intern = new Intern(internApplication.getFirstName(), internApplication.getLastName(),
                        internApplication.getContactNo(), internApplication.getEmail(),
                        internApplication.getCollegeName(), internApplication.getJoiningDate(),
                        internApplication.getCompletionDate(), internApplication.getBranch(), internApplication.getDegree(),
                        internApplication.getPassword(), internApplication.getCollegeIcardImage(),
                        internApplication.getNocPdf(), internApplication.getResumePdf(), internApplication.getPassportSizeImage(),
                        internApplication.getSemester(), internApplication.getDomain(), group);

                intern.setInternId(generateInternId());
                internService.addIntern(intern);
            }

        }
        return "redirect:/bisag/admin/create_group";
    }

    // Add dynamic fields(college, branch)

    @GetMapping("/add_fields")
    public ModelAndView addFields(Model model) {
        ModelAndView mv = new ModelAndView();
        List<College> colleges = fieldService.getColleges();
        List<Branch> branches = fieldService.getBranches();
        List<Domain> domains = fieldService.getDomains();
        List<Degree> degrees = fieldService.getDegrees();
        model = countNotifications(model);
        mv.addObject("colleges", colleges);
        mv.addObject("branches", branches);
        mv.addObject("domains", domains);
        mv.addObject("degrees", degrees);
        mv.setViewName("admin/add_fields");
        return mv;
    }

    @PostMapping("/add_college")
    public String addCollege(College college, Model model) {
        fieldService.addCollege(college);
        return "redirect:/bisag/admin/add_fields";
    }


    @PostMapping("/add_domain")
    public String addDomain(Domain domain, Model model) {
        fieldService.addDomain(domain);
        return "redirect:/bisag/admin/add_fields";
    }

    @PostMapping("/add_branch")
    public String addBranch(Branch branch, Model model) {
        fieldService.addBranch(branch);
        return "redirect:/bisag/admin/add_fields";
    }

    @PostMapping("/add_degree")
    public String addDegree(Degree degree, Model model) {
        fieldService.addDegree(degree);
        return "redirect:/bisag/admin/add_fields";
    }

    // Guide Allocation///////////////////////////
    @GetMapping("/manage_group")
    public ModelAndView allocateGuide(Model model) {
        ModelAndView mv = new ModelAndView("admin/manage_group");
        List<GroupEntity> group = groupService.getGuideNotAllocatedGroup();
        model = countNotifications(model);
        mv.addObject("groups", group);
        return mv;
    }

    @GetMapping("/delete_college/{id}")
    public String deleteCollege(@PathVariable("id") long id) {
        fieldService.deleteCollege(id);
        return "redirect:/bisag/admin/add_fields";
    }

    @GetMapping("/delete_degree/{id}")
    public String deleteDegree(@PathVariable("id") long id) {
        fieldService.deleteDegree(id);
        return "redirect:/bisag/admin/add_fields";
    }


    @GetMapping("/delete_branch/{id}")
    public String deleteBranch(@PathVariable("id") long id, Model model) {
        fieldService.deleteBranch(id);
        return "redirect:/bisag/admin/add_fields";
    }

    @GetMapping("/delete_domain/{id}")
    public String deleteDomain(@PathVariable("id") long id, Model model) {
        fieldService.deleteDomain(id);
        return "redirect:/bisag/admin/add_fields";
    }

    @PostMapping("/update_college/{id}")
    public String updateCollege(@ModelAttribute("college") College college, @PathVariable("id") long id) {
        Optional<College> existingCollege = fieldService.getCollege(id);
        System.out.println("In college update section");
        if (existingCollege.isPresent()) {
            // If the college exists, update its properties
            College updatedCollege = existingCollege.get();
            updatedCollege.setName(college.getName());
            updatedCollege.setLocation(college.getLocation());
            // Save the updated College entity
            fieldService.updateCollege(updatedCollege);
        }
        return "redirect:/bisag/admin/add_fields";
    }

    @PostMapping("/update_branch/{id}")
    public String updateBranch(@ModelAttribute("branch") Branch branch, @PathVariable("id") long id) {
        Optional<Branch> existingBranch = fieldService.getBranch(id);

        if (existingBranch.isPresent()) {
            // If the college exists, update its properties
            Branch updatedBranch = existingBranch.get();
            updatedBranch.setName(branch.getName());
            // Save the updated College entity
            fieldService.updateBranch(updatedBranch);
        }
        return "redirect:/bisag/admin/add_fields";
    }

    @PostMapping("/update_degree/{id}")
    public String updateDegree(@ModelAttribute("degree") Degree degree, @PathVariable("id") long id) {
        Optional<Degree> existingDegree = fieldService.getDegree(id);

        if (existingDegree.isPresent()) {
            Degree updatedDegree = existingDegree.get();
            updatedDegree.setName(degree.getName());
            fieldService.updateDegree(updatedDegree);
        }
        return "redirect:/bisag/admin/add_fields";
    }

    @PostMapping("/update_domain/{id}")
    public String updateBranch(@ModelAttribute("domain") Domain domain, @PathVariable("id") long id) {
        Optional<Domain> existingDomain = fieldService.getDomain(id);

        if (existingDomain.isPresent()) {
            // If the college exists, update its properties
            Domain updatedDomain = existingDomain.get();
            updatedDomain.setName(domain.getName());
            fieldService.updateDomain(updatedDomain);
        }
        return "redirect:/bisag/admin/add_fields";
    }


    // ----------------------------------- Guide registration
    // ---------------------------------------//

    @GetMapping("/register_guide")
    public String registerGuide(Model model) {
        model = countNotifications(model);
        return "admin/guide_registration";
    }

    @PostMapping("/register_guide")
    public String registerGuide(@ModelAttribute("guide") Guide guide) {
        guideService.registerGuide(guide);
        emailService.sendSimpleEmail(guide.getEmailId(), "Notification: Appointment as Administrator\r\n" + "\r\n"
                        + "Dear " + guide.getName() + "\r\n" + "\r\n"
                        + "I trust this email finds you well. We are pleased to inform you that you have been appointed as an administrator within our organization, effective immediately. Your dedication and contributions to the team have not gone unnoticed, and we believe that your new role will bring value to our operations.\r\n"
                        + "\r\n"
                        + "As an administrator, you now hold a position of responsibility within the organization. We trust that you will approach your duties with diligence, professionalism, and a commitment to upholding the values of our organization.\r\n"
                        + "\r\n"
                        + "It is imperative to recognize the importance of your role and the impact it may have on the functioning of our team. We have confidence in your ability to handle the responsibilities that come with this position and to contribute positively to the continued success of our organization.\r\n"
                        + "\r\n"
                        + "We would like to emphasize the importance of maintaining the highest standards of integrity and ethics in your role. It is expected that you will use your administrative privileges responsibly and refrain from any misuse.\r\n"
                        + "\r\n"
                        + "Should you have any questions or require further clarification regarding your new responsibilities, please do not hesitate to reach out to [Contact Person/Department].\r\n"
                        + "\r\n"
                        + "Once again, congratulations on your appointment as an administrator. We look forward to your continued contributions and success in this elevated role.\r\n"
                        + "\r\n" + "Best regards,\r\n" + "\r\n" + "Your Colleague,\r\n" + "Administrator\r\n" + "1231231231",
                "BISAG ADMINISTRATIVE OFFICE");

        return "redirect:/bisag/admin/guide_list";
    }


    @GetMapping("/guide_list")
    public ModelAndView guideList(Model model) {
        ModelAndView mv = new ModelAndView("admin/guide_list");
        List<Guide> guides = guideService.getGuide();
        model = countNotifications(model);
        mv.addObject("guides", guides);
        return mv;
    }

    @GetMapping("/guide_list/{id}")
    public ModelAndView guideList(@PathVariable("id") long id, Model model) {
        System.out.println("id" + id);
        ModelAndView mv = new ModelAndView();
        Optional<Guide> guide = guideService.getGuide(id);
        model = countNotifications(model);
        mv.addObject("guide", guide);
        mv.setViewName("admin/guide_list_detail");
        return mv;
    }

    @GetMapping("/update_guide/{id}")
    public ModelAndView updateGuide(@PathVariable("id") long id, Model model) {
        ModelAndView mv = new ModelAndView("admin/update_guide");
        Optional<Guide> guide = guideService.getGuide(id);
        model = countNotifications(model);
        mv.addObject("guide", guide.orElse(new Guide()));
        return mv;
    }

    @PostMapping("/update_guide/{id}")
    public String updateGuide(@ModelAttribute("guide") Guide guide, @PathVariable("id") long id) {
        Optional<Guide> existingGuide = guideService.getGuide(guide.getGuideId());

        if (existingGuide.isPresent()) {

            String currentPassword = existingGuide.get().getPassword();
            Guide updatedGuide = existingGuide.get();
            updatedGuide.setName(guide.getName());
            updatedGuide.setLocation(guide.getLocation());
            updatedGuide.setFloor(guide.getFloor());
            updatedGuide.setLabNo(guide.getLabNo());
            updatedGuide.setContactNo(guide.getContactNo());
            updatedGuide.setEmailId(guide.getEmailId());
            if (!currentPassword.equals(encodePassword(guide.getPassword())) && guide.getPassword() != "") {
                updatedGuide.setPassword(encodePassword(guide.getPassword()));
            }
            // Save the updated admin entity
            guideService.updateGuide(updatedGuide, existingGuide);

        }
        return "redirect:/bisag/admin/guide_list";
    }

    // Delete Guide
    @PostMapping("/guide_list/delete/{id}")
    public String deleteAdmin(@PathVariable("id") long id) {
        guideService.deleteGuide(id);
        return "redirect:/bisag/admin/guide_list";
    }

    // Manage group
    @GetMapping("/allocate_guide")
    public ModelAndView manageGroup(Model model) {
        ModelAndView mv = new ModelAndView();
        List<GroupEntity> allocatedGroups = groupService.getAllocatedGroups();
        List<GroupEntity> notAllocatedGroups = groupService.getNotAllocatedGroups();
        List<Intern> interns = internService.getInterns();
        List<Guide> guides = guideService.getGuide();
        model = countNotifications(model);
        mv.setViewName("/admin/allocate_guide");
        mv.addObject("alloactedGroups", allocatedGroups);
        mv.addObject("notAllocatedGroups", notAllocatedGroups);
        mv.addObject("guides", guides);
        mv.addObject("interns", interns);
        return mv;
    }

    // Manage group details
    @GetMapping("/allocate_guide/{id}")
    public ModelAndView manageGroup(@PathVariable("id") String id, Model model) {
        ModelAndView mv = new ModelAndView();
        GroupEntity group = groupService.getGroup(id);
        List<Intern> interns = internService.getInterns();
        List<Guide> guides = guideService.getGuide();
        mv.setViewName("/admin/manage_group_detail");
        model = countNotifications(model);
        mv.addObject("groups", group);
        mv.addObject("guides", guides);
        mv.addObject("interns", interns);
        return mv;
    }

    @PostMapping("/allocate_guide/assign_guide")
    public String assignGuide(@RequestParam("guideid") long guideid, @RequestParam("groupId") String groupId) {
        System.out.println("guide id: " + guideid);
        groupService.assignGuide(groupId, guideid);
        return "redirect:/bisag/admin/allocate_guide";
    }

    // Project Definition Approvals
    @GetMapping("/admin_pending_def_approvals")
    public ModelAndView pendingFromGuide(Model model) {
        ModelAndView mv = new ModelAndView("/admin/admin_pending_def_approvals");
        List<GroupEntity> groups = groupService.getAPendingGroups();
        model = countNotifications(model);
        mv.addObject("groups", groups);
        return mv;
    }

    @PostMapping("/admin_pending_def_approvals/{groupId}")
    public String pendingFromAdmin(@RequestParam("apendingAns") String apendingAns,
                                   @PathVariable("groupId") String groupId, @RequestParam("projectDefinition") String projectDefinition, @RequestParam("description") String description) {

        GroupEntity group = groupService.getGroup(groupId);
        if (group != null) {
            group.setProjectDefinition(projectDefinition);
            group.setDescription(description);

            if (apendingAns.equals("approve")) {
                group.setProjectDefinitionStatus("approved");
                List<Intern> interns = internService.getInternsByGroupId(group.getId());
                for (Intern intern : interns) {
                    intern.setProjectDefinitionName(group.getProjectDefinition());
                    internRepo.save(intern);
                }
            } else {
                group.setProjectDefinitionStatus("pending");
            }
            groupRepo.save(group);
        } else {
            // Handle the case where the group is null, perhaps by logging an error or returning an error response
        }
        return "redirect:/bisag/admin/admin_pending_def_approvals";
    }

    @GetMapping("admin_weekly_report")
    public ModelAndView weeklyReport(Model model) {
        ModelAndView mv = new ModelAndView("/admin/admin_weekly_report");
        List<GroupEntity> groups = groupService.getAllocatedGroups();
        List<WeeklyReport> reports = weeklyReportService.getAllReports();
        groups.sort(Comparator.comparing(GroupEntity::getGroupId));
        model = countNotifications(model);
        mv.addObject("groups", groups);
        mv.addObject("reports", reports);
        return mv;
    }

    @GetMapping("/admin_weekly_report_details/{groupId}/{weekNo}")
    public ModelAndView chanegWeeklyReportSubmission(@PathVariable("groupId") String groupId, @PathVariable("weekNo") int weekNo, Model model) {
        ModelAndView mv = new ModelAndView("/admin/admin_weekly_report_details");
        model = countNotifications(model);
        Admin admin = getSignedInAdmin();
        GroupEntity group = groupService.getGroup(groupId);
        WeeklyReport report = weeklyReportService.getReportByWeekNoAndGroupId(weekNo, group);
        MyUser user = myUserService.getUserByUsername(admin.getEmailId());
        if (user.getRole().equals("ADMIN")) {

            String name = admin.getName();
            mv.addObject("replacedBy", name);

        } else if (user.getRole().equals("INTERN")) {
            Intern intern = internService.getInternByUsername(user.getUsername());
            mv.addObject("replacedBy", intern.getFirstName() + intern.getLastName());
        } else {
        }
        mv.addObject("report", report);
        mv.addObject("group", group);

        return mv;
    }


    @GetMapping("/cancellation_requests")
    public ModelAndView cancellationRequests(Model model) {
        ModelAndView mv = new ModelAndView("/admin/cancellation_requests");
        List<Intern> requestedInterns = internService.getInternsByCancellationStatus("requested");
        model = countNotifications(model);
        // Add the list of requested interns to the ModelAndView
        mv.addObject("requestedInterns", requestedInterns);

        return mv;
    }

    @PostMapping("/cancellation_requests/ans")
    public String pendingCancellationsFromAdmin(@RequestParam("cancelAns") String cancelAns,
                                                @RequestParam("internId") String internId) {

        adminService.cancelIntern(cancelAns, internId);

        return "redirect:/bisag/admin/cancellation_requests";
    }

    @GetMapping("/query_to_guide")
    public ModelAndView queryToGuide(Model model) {
        ModelAndView mv = new ModelAndView("/admin/query_to_guide");
        List<Admin> admins = adminService.getAdmin();
        List<Guide> guides = guideService.getGuide();
        List<Intern> interns = internService.getInterns();
        List<GroupEntity> groups = groupService.getAllocatedGroups();
        model = countNotifications(model);
        mv.addObject("groups", groups);
        mv.addObject("interns", interns);
        mv.addObject("admins", admins);
        mv.addObject("guides", guides);
        return mv;
    }

    @GetMapping("/manage_forms")
    public ModelAndView manageForms(Model model) {
        ModelAndView mv = new ModelAndView("/admin/manage_forms");
        List<Intern> interns = internService.getInterns();
        model = countNotifications(model);
        mv.addObject("interns", interns);
        return mv;
    }

    @GetMapping("/admin_pending_final_reports")
    public ModelAndView adminPendingFinalReports(Model model) {
        ModelAndView mv = new ModelAndView("/admin/admin_pending_final_reports");
        List<GroupEntity> groups = groupService.getAPendingFinalReports();
        model = countNotifications(model);
        mv.addObject("groups", groups);
        return mv;
    }

    @PostMapping("/admin_pending_final_reports/ans")
    public String adminPendingFinalReports(@RequestParam("apendingAns") String apendingAns,
                                           @RequestParam("groupId") String groupId) {
        GroupEntity group = groupService.getGroup(groupId);
        if (apendingAns.equals("approve")) {
            group.setFinalReportStatus("approved");
        } else {
            group.setFinalReportStatus("pending");
        }
        groupRepo.save(group);
        return "redirect:/bisag/admin/admin_pending_final_reports";
    }

    @GetMapping("/manage_leave_applications")
    public ModelAndView manageLeaveApplications(Model model) {
        ModelAndView mv = new ModelAndView("/admin/manage_leave_applications");
        List<Intern> interns = internService.getInterns();
        model = countNotifications(model);
        mv.addObject("interns", interns);
        return mv;
    }

    @GetMapping("/manage_leave_applications_details/{id}")
    public ModelAndView manageLeaveApplicationsDetails(Model model) {
        ModelAndView mv = new ModelAndView("/admin/manage_leave_applications_details");
        List<Intern> interns = internService.getInterns();
        model = countNotifications(model);
        mv.addObject("interns", interns);
        return mv;
    }

    @GetMapping("/generate_intern_report")
    public ModelAndView generateInternReport(Model model) {
        ModelAndView mv = new ModelAndView("/admin/generate_intern_report");
        List<College> college = fieldService.getColleges();
        List<Branch> branch = fieldService.getBranches();
        List<Domain> domain = fieldService.getDomains();
        List<Guide> guide = guideService.getGuide();
        // List<Cancelled> cancelled = cancelledService.getCancelledIntern();
        model = countNotifications(model);
        mv.addObject("colleges", college);
        mv.addObject("branches", branch);
        mv.addObject("domains", domain);
        mv.addObject("guides", guide);
        // mv.addObject("cancelled",cancelled);
        return mv;
    }

    @PostMapping("/generate_intern_report")
    public String generateInternReport(HttpServletResponse response, @ModelAttribute("ReportFilter") ReportFilter reportFilter) throws Exception {
        College college;
        Branch branch;
        Optional<Guide> guide;
        Domain domain;
        if (reportFilter.getBranch().equals("All")) {
            reportFilter.setBranch(null);
        } else {
            branch = fieldService.findByBranchName(reportFilter.getBranch());
        }

        if (reportFilter.getCollege().equals("All")) {
            reportFilter.setCollege(null);
        } else {
            college = fieldService.findByCollegeName(reportFilter.getCollege());
        }

        if (reportFilter.getGuide().equals("All")) {
            guide = null;
        } else {
            guide = guideService.getGuideByName(reportFilter.getGuide());
        }

        if (reportFilter.getDomain().equals("All")) {
            reportFilter.setDomain(null);
        } else {
            domain = fieldService.getDomainByName(reportFilter.getDomain());
        }

        List<Intern> filteredInterns = internService.getFilteredInterns(reportFilter.getCollege(),
                reportFilter.getBranch(), guide, reportFilter.getDomain(), reportFilter.getCancelled(),
                reportFilter.getStartDate(), reportFilter.getEndDate(), reportFilter.getCancelled());

        for (Intern intern : filteredInterns) {
            System.out.println(intern.getFirstName());
        }
        if (reportFilter.getFormat().equals("pdf")) {
            dataExportService.exportToPdf(filteredInterns, response);
        } else {
            dataExportService.exportToExcel(filteredInterns, response);
        }
        return "redirect:/bisag/admin/admin_dashboard";
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam("newPassword") String newPassword) {
        Admin admin = getSignedInAdmin();
        adminService.changePassword(admin, newPassword);
        return "redirect:/logout";
    }
}
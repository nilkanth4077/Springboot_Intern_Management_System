package com.rh4.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.rh4.entities.Admin;
import com.rh4.entities.GroupEntity;
import com.rh4.entities.Guide;
import com.rh4.entities.Intern;
import com.rh4.entities.InternApplication;
import com.rh4.entities.MyUser;
import com.rh4.entities.WeeklyReport;
import com.rh4.models.ProjectDefinition;
import com.rh4.repositories.GroupRepo;
import com.rh4.services.AdminService;
import com.rh4.services.GuideService;
import com.rh4.services.InternService;
import com.rh4.services.MyUserService;
import com.rh4.services.WeeklyReportService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bisag/intern")
public class InternController {

    @Value("${app.storage.base-dir}")
    private String baseDir;

    @Value("${app.storage.base-dir2}")
    private String baseDir2;

    @Autowired
    private InternService internService;
    @Autowired
    private WeeklyReportService weeklyReportService;
    @Autowired
    private MyUserService myUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private GuideService guideService;
    @Autowired
    HttpSession session;
    @Autowired
    GroupRepo groupRepo;
    boolean WEEKLYREPORTDISABLE;
    int CurrentWeekNo;

    public Intern getSignedInIntern() {
        String username = (String) session.getAttribute("username");
        Intern intern = internService.getInternByUsername(username);
        if (intern.getIsActive()) {
            return intern;
        } else {
            return null;
        }
    }

    public String getUsername() {
        String username = (String) session.getAttribute("username");
        return username;
    }

    public Date getNextSubmissionDate() {
        Intern intern = getSignedInIntern();
        GroupEntity group = intern.getGroup();
        List<Intern> interns = internService.getInternsByGroupId(group.getId());
        Date oldestJoiningDate = null;

        for (Intern i : interns) {
            Date joiningDate = i.getJoiningDate();

            // Check if the oldestJoiningDate is null or if the current intern's joining
            // date is older
            if (oldestJoiningDate == null || joiningDate.before(oldestJoiningDate)) {
                oldestJoiningDate = joiningDate;
            }
        }

        System.out.println("Oldest joining date from each intern: " + oldestJoiningDate);

        Integer recentWeekNo = (Integer) weeklyReportService.getRecentWeekNo(group);
        System.out.println(recentWeekNo);

        // Calculate next submission date based on recentWeekNo and oldestJoiningDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldestJoiningDate);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, (recentWeekNo) * 7); // Add 6 weeks for each week number
        // compare with completion date

        return calendar.getTime();
    }

    public void checkWeeklyReportDisable() {
        if (youngestCompletionDate().before(new Date()))
            WEEKLYREPORTDISABLE = true;
        else
            WEEKLYREPORTDISABLE = false;
    }

    public Date youngestCompletionDate() {
        Intern intern = getSignedInIntern();
        GroupEntity group = intern.getGroup();
        List<Intern> interns = internService.getInternsByGroupId(group.getId());
        Date youngestCompletionDate = null;

        for (Intern i : interns) {
            Date completionDate = i.getCompletionDate();

            // Check if the oldestJoiningDate is null or if the current intern's joining
            // date is older
            if (youngestCompletionDate == null || completionDate.after(youngestCompletionDate)) {
                youngestCompletionDate = completionDate;
            }
        }
        return youngestCompletionDate;
    }

    public Date checkLastWeeklyReportSubmissionDate(Date nextSubmissionDate) {
        System.out.println("youngest completion date from each intern: " + youngestCompletionDate());

        if (nextSubmissionDate.after(youngestCompletionDate())) {
            return youngestCompletionDate();
        } else if (nextSubmissionDate.before(youngestCompletionDate())) {
            return nextSubmissionDate;
        } else
            return null;
    }

    public Date addDaysToYoungestCompletionDate() {
        Date youngestCompletionDate = youngestCompletionDate(); // Assuming this method returns a Date object
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(youngestCompletionDate);
        calendar.add(Calendar.DAY_OF_MONTH, 7); // Add 7 days
        return calendar.getTime();
    }

    @GetMapping("/intern_dashboard")
    public ModelAndView intern_dashboard(HttpSession session, Model model) {

        ModelAndView mv = new ModelAndView("intern/intern_dashboard");
        Intern intern = getSignedInIntern();
        String username = getUsername();
        InternApplication internApplication = internService.getInternApplicationByUsername(username);
        // Add group details to the ModelAndView
        if (intern.getGroupEntity() != null) {
            mv.addObject("group", intern.getGroupEntity());
            List<Intern> interns = internService.getInternsByGroupId(intern.getGroup().getId());
            mv.addObject("interns", interns);
            int internCountGroupWise = interns.size();
            mv.addObject("internCountGroupWise", internCountGroupWise);
        } else {
            mv.addObject("group", null); // Handle the case when no group is assigned
        }

        // Set the "id" and "username" attributes in the session
        session.setAttribute("id", intern.getInternId());
        session.setAttribute("username", username);

        // Add the username to the ModelAndView
        mv.addObject("username", username);

        // Add intern details to the ModelAndView
        mv.addObject("intern", intern);
        mv.addObject("internApplication", internApplication);

        return mv;
    }

    @PostMapping("/requestCancellation")
    public String requestCancellation(HttpSession session) {
        Intern intern = getSignedInIntern();
        intern.setCancellationStatus("requested");
        internService.updateCancellationStatus(intern);
        return "redirect:/bisag/intern/intern_dashboard";
    }

    // project def approval
    @GetMapping("/project_definition")
    public ModelAndView project_definition(HttpSession session, Model model) {

        ModelAndView mv = new ModelAndView("/intern/project_definition");
        String username = getUsername();
        Intern intern = getSignedInIntern();
        if (intern.getGroupEntity() != null) {
            mv.addObject("group", intern.getGroupEntity());
        } else {
            mv.addObject("group", null); // Handle the case when no group is assigned
        }
        mv.addObject("intern", getSignedInIntern());
        return mv;

    }

    @PostMapping("/project_definition_submission")
    public String approveProjectDefinition(@RequestParam("projectDefinition") String projectDefinition,
                                           @RequestParam("description") String description,
                                           @RequestParam("projectDefinitionDocument") MultipartFile projectDefinitionDocument
    )
            throws Exception {
        Intern intern = getSignedInIntern();
        GroupEntity group = intern.getGroup();

        String storageDir = baseDir2 + group.getGroupId() + "/";
        File directory = new File(storageDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String projectDefinitionDocumentFileName = storageDir + "projectDefinitionDocument.pdf";

        Files.write(Paths.get(projectDefinitionDocumentFileName), projectDefinitionDocument.getBytes());

        group.setProjectDefinition(projectDefinition);
        group.setDescription(description);
        group.setProjectDefinitionDocument(projectDefinitionDocument.getBytes());
        group.setProjectDefinitionStatus("gpending");
        groupRepo.save(group);
        return "redirect:/bisag/intern/project_definition";
    }

    @GetMapping("/weekly_report_submission")
    public ModelAndView weeklyReportSubmission() {
        ModelAndView mv = new ModelAndView("intern/weekly_report_submission");
        Date nextSubmissionDate = getNextSubmissionDate();
        Intern intern = getSignedInIntern();
        GroupEntity group = intern.getGroup();
        Integer nextSubmissionWeekNo = (Integer) weeklyReportService.getRecentWeekNo(group);
        List<WeeklyReport> weeklyReports = weeklyReportService.getReportsByGroupId(group.getId());
        checkWeeklyReportDisable();
        String weeklyReportDisable1;
        if (WEEKLYREPORTDISABLE) {
            weeklyReportDisable1 = "true";
        } else {
            weeklyReportDisable1 = "false";
        }

		// Retrieve the last weekly report
        WeeklyReport lastWeeklyReport = null;
        if (!weeklyReports.isEmpty()) {
            lastWeeklyReport = weeklyReports.get(weeklyReports.size() - 1);
        }

        // Extract deadline of the last weekly report if it exists
        Date deadlineOfLastWeeklyReport = null;
        String weeklyReportDisable2;
        if (lastWeeklyReport != null) {
            deadlineOfLastWeeklyReport = lastWeeklyReport.getDeadline();
            LocalDate localDate = deadlineOfLastWeeklyReport.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = localDate.format(formatter);

            System.out.println(formattedDate); // Print the formatted date

            String checkWithFormattedDate = checkLastWeeklyReportSubmissionDate(nextSubmissionDate).toString();
            System.out.println(checkWithFormattedDate);
            if (formattedDate.equals(checkWithFormattedDate)) {
                weeklyReportDisable2 = "true";
            } else {
                weeklyReportDisable2 = "false";
            }
            mv.addObject("weeklyReportDisable2", weeklyReportDisable2);
        }

        mv.addObject("nextSubmissionDate", checkLastWeeklyReportSubmissionDate(nextSubmissionDate));
        mv.addObject("nextSubmissionWeekNo", nextSubmissionWeekNo);
        mv.addObject("weeklyReports", weeklyReports);
        mv.addObject("intern", intern);
        mv.addObject("group", group);
        mv.addObject("weeklyReportDisable1", weeklyReportDisable1);
        return mv;
    }

    @PostMapping("/weekly_report_submission")
    public String weeklyReportSubmission(@RequestParam("currentWeekNo") int currentWeekNo,
                                         @RequestParam("weeklyReportSubmission") MultipartFile weeklyReportSubmission) throws IllegalStateException, IOException, Exception {
        Intern intern = getSignedInIntern();
        GroupEntity group = intern.getGroup();
        Date currentDate = new Date();
        WeeklyReport weeklyReport = new WeeklyReport();
        CurrentWeekNo = currentWeekNo;

        String storageDir = baseDir2 + intern.getGroup().getGroupId() + "/Weekly Reports";
        String storageDir2 = storageDir + "/" + intern.getGroup().getGroupId() + "_" + "Week" + currentWeekNo;
        File directory = new File(storageDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String weeklyReportFileName = storageDir2 + ".pdf";

        Files.write(Paths.get(weeklyReportFileName), weeklyReportSubmission.getBytes());

        weeklyReport.setGroup(group);
        weeklyReport.setGuide(group.getGuide());
        weeklyReport.setIntern(intern);
        weeklyReport.setReportSubmittedDate(currentDate);
        weeklyReport.setSubmittedPdf(weeklyReportSubmission.getBytes());
        weeklyReport.setWeekNo(currentWeekNo);

        Date updatedNextSubmissionDate = checkLastWeeklyReportSubmissionDate(getNextSubmissionDate());

        weeklyReport.setDeadline(updatedNextSubmissionDate);
        MyUser user = myUserService.getUserByUsername(intern.getEmail());
        weeklyReport.setReplacedBy(user);
        // Check if the deadline is greater than or equal to the reportSubmittedDate
        if (weeklyReport.getDeadline().compareTo(currentDate) >= 0) {
            // If the deadline is greater than or equal to the reportSubmittedDate, set the
            // status to "submitted"
            weeklyReport.setStatus("submitted");
        } else {
            // If the deadline is less than the reportSubmittedDate, set the status to "late
            // submitted"
            weeklyReport.setStatus("late submitted");
        }
        weeklyReportService.addReport(weeklyReport);
        return "redirect:/bisag/intern/weekly_report_submission";
    }

    @GetMapping("/change_weekly_report/{weekNo}")
    public ModelAndView chanegWeeklyReportSubmission(@PathVariable("weekNo") int weekNo) {
        ModelAndView mv = new ModelAndView("intern/change_weekly_report");
        Intern inetrn = getSignedInIntern();
        GroupEntity group = inetrn.getGroup();
        WeeklyReport report = weeklyReportService.getReportByWeekNoAndGroupId(weekNo, group);
        MyUser user = myUserService.getUserByUsername(report.getReplacedBy().getUsername());
        if (user.getRole().equals("GUIDE")) {
            Guide guide = guideService.getGuideByUsername(user.getUsername());
            String status = "Your Current Weekly report is required some modifications given by guide. Please check it out.";
            mv.addObject("status", status);
            mv.addObject("replacedBy", guide.getName());
        } else if (user.getRole().equals("INTERN")) {
            Intern intern = internService.getInternByUsername(user.getUsername());
            mv.addObject("replacedBy", intern.getFirstName() + " " + intern.getLastName());
            mv.addObject("status",
                    "Your current weekly report is accepted and if any changes are required then you will be notified.");
        }
        mv.addObject("report", report);
        mv.addObject("group", group);
        return mv;
    }

    @GetMapping("/viewPdf/{internId}/{weekNo}")
    public ResponseEntity<byte[]> viewPdf(@PathVariable String internId, @PathVariable int weekNo) {
        WeeklyReport report = weeklyReportService.getReportByInternIdAndWeekNo(internId, weekNo);
        byte[] pdfContent = report.getSubmittedPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    @PostMapping("/change_weekly_report/changed_report")
    public String chanegWeeklyReportSubmission(@RequestParam("weekNo") int weekNo, @RequestParam("weeklyReportSubmission") MultipartFile weeklyReportSubmission)
            throws IllegalStateException, IOException, Exception {
        Intern intern = getSignedInIntern();
        GroupEntity group = intern.getGroup();
        WeeklyReport report = weeklyReportService.getReportByWeekNoAndGroupId(weekNo, group);
        CurrentWeekNo = weekNo;
        report.setSubmittedPdf(weeklyReportSubmission.getBytes());

        String storageDir = baseDir2 + intern.getGroup().getGroupId() + "/Weekly Reports/" + intern.getGroup().getGroupId() + "_Week" + weekNo + ".pdf";
        File existingFile = new File(storageDir);

        if (existingFile.exists()) {
            existingFile.delete();
        }

        Files.write(Paths.get(storageDir), weeklyReportSubmission.getBytes());

        MyUser user = myUserService.getUserByUsername(intern.getEmail());
        report.setReplacedBy(user);
        Date currentDate = new Date();
        // Check if the deadline is greater than or equal to the reportSubmittedDate
        if (report.getDeadline().compareTo(currentDate) >= 0) {
            // If the deadline is greater than or equal to the reportSubmittedDate, set the
            // status to "submitted"
            report.setStatus("submitted");
        } else {
            // If the deadline is less than the reportSubmittedDate, set the status to "late
            // submitted"
            report.setStatus("late submitted");
        }

        weeklyReportService.addReport(report);
        return "redirect:/bisag/intern/change_weekly_report/" + weekNo;
    }

    @GetMapping("/submit_forms")
    public ModelAndView submitForms() {
        ModelAndView mv = new ModelAndView("intern/submit_forms");
        Intern intern = getSignedInIntern();

        mv.addObject("intern", intern);
        return mv;
    }

    @PostMapping("/submit_forms")
    public String submitForms(@RequestParam("permanentAddress") String permanentAddress,
                              @RequestParam("dateOfBirth") java.sql.Date dateOfBirth,
                              @RequestParam("gender") String gender,
                              @RequestParam("collegeGuideHodName") String collegeGuideHodName,
                              @RequestParam("aggregatePercentage") Double aggregatePercentage,
                              @RequestParam("usedResource") String usedResource,
                              @RequestParam("registrationForm") MultipartFile registrationForm,
                              @RequestParam("securityForm") MultipartFile securityForm,
                              @RequestParam("icardForm") MultipartFile icardForm) throws IllegalStateException, IOException, Exception {
        System.out.println("called sub");
        Intern intern = getSignedInIntern();

        String storageDir = baseDir + intern.getEmail() + "/";
        File directory = new File(storageDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save files to local storage
        String registrationFormName = storageDir + "registrationForm.pdf";
        String securityFormName = storageDir + "securityForm.pdf";
        String icardFormName = storageDir + "icardForm.pdf";

        // Save Passport Size Image
        Files.write(Paths.get(registrationFormName), registrationForm.getBytes());

        // Save College Icard Image
        Files.write(Paths.get(securityFormName), securityForm.getBytes());

        // Save NOC PDF
        Files.write(Paths.get(icardFormName), icardForm.getBytes());

        intern.setPermanentAddress(permanentAddress);
        intern.setDateOfBirth(dateOfBirth);
        intern.setGender(gender);
        intern.setCollegeGuideHodName(collegeGuideHodName);
        intern.setAggregatePercentage(aggregatePercentage);
        intern.setUsedResource(usedResource);
        intern.setIcardForm(icardForm.getBytes());
        intern.setSecurityForm(securityForm.getBytes());
        intern.setRegistrationForm(registrationForm.getBytes());
        internService.registerIntern(intern);
        return "redirect:/bisag/intern/submit_forms";
    }

    @GetMapping("/documents/icardForm/{id}")
    public ResponseEntity<byte[]> getICardFormForIntern(@PathVariable("id") String id) {
        Optional<Intern> optionalApplication = internService.getIntern(id);

        if (optionalApplication.isPresent()) {
            Intern application = optionalApplication.get();

            byte[] pdf = application.getIcardForm();

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

    @GetMapping("/documents/security/{id}")
    public ResponseEntity<byte[]> getSecurityFormForIntern(@PathVariable("id") String id) {
        Optional<Intern> optionalApplication = internService.getIntern(id);

        if (optionalApplication.isPresent()) {
            Intern application = optionalApplication.get();

            byte[] pdf = application.getSecurityForm();

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

    @GetMapping("/documents/registration/{id}")
    public ResponseEntity<byte[]> getRegistrationFormForIntern(@PathVariable("id") String id) {
        Optional<Intern> optionalApplication = internService.getIntern(id);

        if (optionalApplication.isPresent()) {
            Intern application = optionalApplication.get();

            byte[] pdf = application.getRegistrationForm();

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
    public ResponseEntity<byte[]> getResumePdfForIntern(@PathVariable("id") String id) {
        Optional<Intern> optionalApplication = internService.getIntern(id);

        if (optionalApplication.isPresent()) {
            Intern application = optionalApplication.get();

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

    @GetMapping("/documents/noc/{id}")
    public ResponseEntity<byte[]> getNocPdfForIntern(@PathVariable("id") String id) {
        Optional<Intern> optionalApplication = internService.getIntern(id);

        if (optionalApplication.isPresent()) {
            Intern application = optionalApplication.get();

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

    @GetMapping("/documents/icard/{id}")
    public ResponseEntity<byte[]> getICardImageForIntern(@PathVariable("id") String id) {
        Optional<Intern> optionalApplication = internService.getIntern(id);

        if (optionalApplication.isPresent()) {
            Intern application = optionalApplication.get();

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

    @GetMapping("/documents/passport/{id}")
    public ResponseEntity<byte[]> getPassportSizeImageForIntern(@PathVariable("id") String id) {
        Optional<Intern> optionalApplication = internService.getIntern(id);

        if (optionalApplication.isPresent()) {
            Intern application = optionalApplication.get();

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

    @GetMapping("/query")
    public ModelAndView query() {
        ModelAndView mv = new ModelAndView("/intern/query");
        List<Admin> admins = adminService.getAdmin();
        List<Guide> guides = guideService.getGuide();
        Intern intern = getSignedInIntern();
        if (intern.getGroupEntity() != null) {
            mv.addObject("group", intern.getGroupEntity());
        } else {
            mv.addObject("group", null); // Handle the case when no group is assigned
        }
        mv.addObject("admins", admins);
        mv.addObject("guides", guides);
        mv.addObject("intern", getSignedInIntern());
        return mv;
    }

    @GetMapping("/final_report_submission")
    public ModelAndView finalReportSubmission(HttpSession session, Model model) {

        ModelAndView mv = new ModelAndView("/intern/final_report_submission");
        String username = getUsername();
        Intern intern = getSignedInIntern();
        if (intern.getGroupEntity() != null) {
            mv.addObject("group", intern.getGroupEntity());
        } else {
            mv.addObject("group", null);
        }
        Date deadlineOfFinalReport = addDaysToYoungestCompletionDate();
        String submitDisable;
        if (deadlineOfFinalReport.after(new Date())) {
            submitDisable = "false";
        } else {
            submitDisable = "true";
        }
        mv.addObject("intern", intern);
        mv.addObject("deadline", deadlineOfFinalReport);
        mv.addObject("submitDisable", submitDisable);
        return mv;

    }

    @PostMapping("/final_report_submission")
    public String finalReportSubmission(@RequestParam("finalReport") MultipartFile finalReport) throws Exception {
        Intern intern = getSignedInIntern();
        GroupEntity group = intern.getGroup();
        String storageDir = baseDir + intern.getEmail() + "/";
        File directory = new File(storageDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String finalReportFileName = storageDir + "finalReport.pdf";

        Files.write(Paths.get(finalReportFileName), finalReport.getBytes());
        group.setFinalReport(finalReport.getBytes());
        group.setFinalReportStatus("gpending");
        groupRepo.save(group);
        return "redirect:/bisag/intern/final_report_submission";
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam("newPassword") String newPassword) {
        Intern intern = getSignedInIntern();
        internService.changePassword(intern, newPassword);
        return "redirect:/logout";
    }

    @GetMapping("/apply_leave")
    public ModelAndView applyLeave() {
        ModelAndView mv = new ModelAndView("intern/apply_leave");
        mv.addObject("intern", getSignedInIntern());
        return mv;
    }

}
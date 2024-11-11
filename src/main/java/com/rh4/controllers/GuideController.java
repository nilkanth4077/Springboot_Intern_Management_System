package com.rh4.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.rh4.entities.Admin;
import com.rh4.entities.GroupEntity;
import com.rh4.entities.Guide;
import com.rh4.entities.Intern;
import com.rh4.entities.MyUser;
import com.rh4.entities.WeeklyReport;
import com.rh4.repositories.GroupRepo;
import com.rh4.services.AdminService;
import com.rh4.services.GroupService;
import com.rh4.services.GuideService;
import com.rh4.services.InternService;
import com.rh4.services.MyUserService;
import com.rh4.services.WeeklyReportService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bisag/guide")
public class GuideController {

    @Autowired
    HttpSession session;
    @Autowired
    private GuideService guideService;
    @Autowired
    private InternService internService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private WeeklyReportService weeklyReportService;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private MyUserService myUserService;
    Intern internFromUploadFileMethod;
    int CurrentWeekNo;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public Guide getSignedInGuide() {
        String username = (String) session.getAttribute("username");
        Guide guide = guideService.getGuideByUsername(username);
        return guide;
    }

    public String getUsername() {
        String username = (String) session.getAttribute("username");
        return username;
    }

    @GetMapping("/guide_dashboard")
    public ModelAndView guide_dashboard(HttpSession session, Model model) {

        ModelAndView mv = new ModelAndView("guide/guide_dashboard");

        Guide guide = getSignedInGuide();
        String username = getUsername();

        long gPendingCount = groupService.countGPendingGroups();
        mv.addObject("gPendingCount", gPendingCount);

        // Set the "id" and "username" attributes in the session
        session.setAttribute("id", guide.getGuideId());
        session.setAttribute("username", username);

        // Add the username to the ModelAndView
        mv.addObject("username", username);

        // Add intern details to the ModelAndView
        mv.addObject("guide", guide);

        return mv;
    }

    //Intern Groups
    @GetMapping("/intern_groups")
    public ModelAndView internGroups(HttpSession session, Model model) {

        ModelAndView mv = new ModelAndView("/guide/intern_groups");
        Guide guide = getSignedInGuide();
        List<GroupEntity> internGroups = guideService.getInternGroups(guide);
        List<Intern> interns = internService.getInterns();
        mv.addObject("internGroups", internGroups);
        mv.addObject("intern", interns);
        mv.addObject("guide", getSignedInGuide());
        return mv;
    }

    @GetMapping("/intern_groups/{id}")
    public ModelAndView internGroups(@PathVariable("id") String id) {

        ModelAndView mv = new ModelAndView("/guide/intern_groups_detail");
        Guide guide = getSignedInGuide();
        List<GroupEntity> internGroups = guideService.getInternGroups(guide);
        mv.addObject("internGroups", internGroups);
        return mv;

    }

    @GetMapping("/intern/{id}")
    public ModelAndView internDetails(@PathVariable("id") String id) {
        ModelAndView mv = new ModelAndView();
        Optional<Intern> intern = internService.getIntern(id);
        mv.addObject("intern", intern);
        mv.setViewName("guide/intern_detail");
        return mv;
    }

    @GetMapping("/update_guide/{id}")
    public ModelAndView updateGuide(@PathVariable("id") long id) {
        ModelAndView mv = new ModelAndView("admin/update_guide");
        Optional<Guide> guide = guideService.getGuide(id);
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

            // Save the updated admin entity
            guideService.updateGuide(updatedGuide, existingGuide);

        }
        return "redirect:/logout";
    }

    @GetMapping("/guide_pending_def_approvals")
    public ModelAndView pendingFromGuide(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("/guide/guide_pending_def_approvals");
        Guide guide = getSignedInGuide();
        List<GroupEntity> groups = groupService.getGPendingGroups(guide);
        mv.addObject("groups", groups);
        mv.addObject("guide", guide);
        return mv;
    }

    @PostMapping("/guide_pending_def_approvals/ans")
    public String pendingFromGuide(@RequestParam("gpendingAns") String gpendingAns, @RequestParam("groupId") String groupId) {

        GroupEntity group = groupService.getGroup(groupId);
        if (gpendingAns.equals("approve")) {
            group.setProjectDefinitionStatus("gapproved");
        } else {
            group.setProjectDefinitionStatus("pending");
        }
        groupRepo.save(group);
        return "redirect:/bisag/guide/guide_pending_def_approvals";
    }

    @GetMapping("/admin_pending_def_approvals")
    public ModelAndView pendingFromAdmin() {
        ModelAndView mv = new ModelAndView();
        List<GroupEntity> groups = groupService.getAPendingGroups();
        mv.addObject("groups", groups);
        return mv;
    }

    @GetMapping("/weekly_report")
    public ModelAndView weeklyReport() {
        ModelAndView mv = new ModelAndView("/guide/weekly_report");
        Guide guide = getSignedInGuide();
        List<GroupEntity> groups = guideService.getInternGroups(guide);
        List<WeeklyReport> reports = weeklyReportService.getReportsByGuideId(guide.getGuideId());
        mv.addObject("groups", groups);
        mv.addObject("reports", reports);
        mv.addObject("guide", getSignedInGuide());
        return mv;
    }

    @GetMapping("/guide_weekly_report_details/{groupId}/{weekNo}")
    public ModelAndView chanegWeeklyReportSubmission(@PathVariable("groupId") String groupId, @PathVariable("weekNo") int weekNo) {
        ModelAndView mv = new ModelAndView("/guide/guide_weekly_report_details");
        Guide guide = getSignedInGuide();
        GroupEntity group = groupService.getGroup(groupId);
        WeeklyReport report = weeklyReportService.getReportByWeekNoAndGroupId(weekNo, group);
        MyUser user = myUserService.getUserByUsername(getSignedInGuide().getEmailId());
        if (user.getRole().equals("GUIDE")) {

            String name = guide.getName();
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

    @PostMapping("/guide_weekly_report_details/{groupid}/changed_report")
    public String chanegWeeklyReportSubmission(@PathVariable("groupid") String groupId, @RequestParam("weekNo") int weekNo, MultipartHttpServletRequest req) throws IllegalStateException, IOException, Exception {
        GroupEntity group = groupService.getGroup(groupId);
        Guide guide = getSignedInGuide();
        WeeklyReport report = weeklyReportService.getReportByWeekNoAndGroupId(weekNo, group);
        CurrentWeekNo = weekNo;
//			report.setSubmittedPdf(changeWeeklyReport(req.getFile("weeklyReportSubmission"), group));
        MyUser user = myUserService.getUserByUsername(guide.getEmailId());
        report.setReplacedBy(user);
        Date currentDate = new Date();
        // Check if the deadline is greater than or equal to the reportSubmittedDate
        if (report.getDeadline().compareTo(currentDate) >= 0) {
            // If the deadline is greater than or equal to the reportSubmittedDate, set the status to "submitted"
            report.setStatus("submitted");
        } else {
            // If the deadline is less than the reportSubmittedDate, set the status to "late submitted"
            report.setStatus("late submitted");
        }

        weeklyReportService.addReport(report);
        return "redirect:/bisag/guide/weekly_report";
    }

    @GetMapping("/guide_pending_final_reports")
    public ModelAndView guidePendingFinalReports(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("/guide/guide_pending_final_reports");
        Guide guide = getSignedInGuide();
        List<GroupEntity> groups = groupService.getGPendingFinalReports(guide);
        mv.addObject("groups", groups);
        mv.addObject("guide", getSignedInGuide());
        return mv;
    }

    @PostMapping("/guide_pending_final_reports/ans")
    public String guidePendingFinalReports(@RequestParam("gpendingAns") String gpendingAns, @RequestParam("groupId") String groupId) {

        GroupEntity group = groupService.getGroup(groupId);
        if (gpendingAns.equals("approve")) {
            group.setFinalReportStatus("gapproved");
        } else {
            group.setFinalReportStatus("pending");
        }
        groupRepo.save(group);
        return "redirect:/bisag/guide/guide_pending_final_reports";
    }
//		private String changeWeeklyReport(MultipartFile file, GroupEntity group) {
//
//			try {
//				File myDir = new File(weeklyReportSubmission + "/"+ group.getGroupId());
//				if(!myDir.exists())
//				{
//					myDir.mkdirs();
//				}
//				if(!file.isEmpty())
//				{
//					file.transferTo(Paths.get(myDir.getAbsolutePath(), group.getGroupId() + "_week_" + CurrentWeekNo + ".pdf"));
//					return group.getGroupId() + "_week_" + CurrentWeekNo + ".pdf";
//				}
//				else
//					return null;
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//				return "redirect:/";
//			}
//		}

    @GetMapping("/query_to_admin")
    public ModelAndView queryToAdmin() {
        ModelAndView mv = new ModelAndView("/guide/query_to_admin");
        List<Admin> admins = adminService.getAdmin();
        List<Guide> guides = guideService.getGuide();
        Guide guide = getSignedInGuide();
        List<GroupEntity> groups = guideService.getInternGroups(guide);
        mv.addObject("admins", admins);
        mv.addObject("guides", guides);
        mv.addObject("groups", groups);
        mv.addObject("guide", getSignedInGuide());
        return mv;
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam("newPassword") String newPassword) {
        Guide guide = getSignedInGuide();
        guideService.changePassword(guide, newPassword);
        return "redirect:/logout";
    }

}
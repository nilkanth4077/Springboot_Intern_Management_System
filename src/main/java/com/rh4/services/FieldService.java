package com.rh4.services;
import com.rh4.entities.Domain;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rh4.repositories.DomainRepo;
import com.rh4.entities.Branch;
import com.rh4.entities.College;
import com.rh4.entities.Degree;
import com.rh4.repositories.BranchRepo;
import com.rh4.repositories.CollegeRepo;
import com.rh4.repositories.DegreeRepo;

@Service
public class FieldService {
	@Autowired
	private CollegeRepo collegeRepo;
	@Autowired
	private DegreeRepo degreeRepo;
	@Autowired
	private DomainRepo domainRepo;
	@Autowired
	private BranchRepo branchRepo;
	
	public void addCollege(College college) {
		collegeRepo.save(college);
	}
	public void addDomain(Domain domain) {
		domainRepo.save(domain);
	}
	public void addBranch(Branch branch) {
		branchRepo.save(branch);
	}
	public void addDegree(Degree degree) {
		degreeRepo.save(degree);
	}
	public List<College> getColleges()
	{
		return collegeRepo.findAll();
	}
	public List<Degree> getDegrees()
	{
		return degreeRepo.findAll();
	}
	public List<Branch> getBranches()
	{
		return branchRepo.findAll();
	}
	public List<Domain> getDomains()
	{
		return domainRepo.findAll();
	}
	
	public void deleteBranch(long id)
	{
		branchRepo.deleteById(id);
	}
	public void deleteDegree(long id)
	{
		degreeRepo.deleteById(id);
	}
	public void deleteDomain(long id)
	{
		domainRepo.deleteById(id);
	}
	public void deleteCollege(long id)
	{
		collegeRepo.deleteById(id);
	}
	
	public Optional<College> getCollege(long id)
	{
		return collegeRepo.findByCollegeId(id);
	}
	public Optional<Degree> getDegree(long id)
	{
		return degreeRepo.findById(id);
	}
	public Optional<Branch> getBranch(long id)
	{
		return branchRepo.findById(id);
	}
	public Optional<Domain> getDomain(long id)
	{
		return domainRepo.findById(id);
	}
	public College findByCollegeName(String name) {
		return collegeRepo.findByName(name);
	}
	public Degree findByDegreeName(String name) {
		return degreeRepo.findByName(name);
	}
	public Branch findByBranchName(String name) {
		return branchRepo.findByName(name);
	}
	public Domain getDomainByName(String name) {
		return domainRepo.findByName(name);
	}
	public void updateBranch(Branch updatedBranch) {
		branchRepo.save(updatedBranch);
	}
	public void updateDomain(Domain updatedDomain) {
		domainRepo.save(updatedDomain);
	}
	public void updateCollege(College college)
	{
		collegeRepo.save(college);
	}
	public void updateDegree(Degree degree)
	{
		degreeRepo.save(degree);
	}
	
}
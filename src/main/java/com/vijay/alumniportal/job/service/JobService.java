package com.vijay.alumniportal.job.service;

import com.vijay.alumniportal.alumni.repository.AlumniRepository;
import com.vijay.alumniportal.job.dto.JobApplicationResponse;
import com.vijay.alumniportal.job.dto.JobRequest;
import com.vijay.alumniportal.job.dto.JobResponse;
import com.vijay.alumniportal.job.entity.Job;
import com.vijay.alumniportal.job.entity.JobApplication;
import com.vijay.alumniportal.job.repository.JobApplicationRepository;
import com.vijay.alumniportal.job.repository.JobRepository;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final AlumniRepository alumniRepository;

    public JobService(
            JobRepository jobRepository,
            JobApplicationRepository applicationRepository,
            StudentRepository studentRepository,
            AlumniRepository alumniRepository
    ) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.alumniRepository = alumniRepository;
    }

    public JobResponse createJob(JobRequest request) {

        alumniRepository.findById(request.getAlumniId())
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + request.getAlumniId()));

        Job job = Job.builder()
                .alumniId(request.getAlumniId())
                .title(request.getTitle())
                .company(request.getCompany())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .skillsRequired(request.getSkillsRequired())
                .appliedCount(0)
                .build();

        return mapToJobResponse(jobRepository.save(job));
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToJobResponse)
                .toList();
    }

    public JobResponse getJobById(Long id) {
        Job job = getJobEntity(id);
        return mapToJobResponse(job);
    }

    public List<JobResponse> getJobsByAlumniId(Long alumniId) {

        alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found with id: " + alumniId));

        return jobRepository.findByAlumniId(alumniId)
                .stream()
                .map(this::mapToJobResponse)
                .toList();
    }

    public JobResponse updateJob(Long id, JobRequest request) {

        Job job = getJobEntity(id);

        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSkillsRequired(request.getSkillsRequired());

        return mapToJobResponse(jobRepository.save(job));
    }

    public void deleteJob(Long id) {
        Job job = getJobEntity(id);
        jobRepository.delete(job);
    }

    public JobApplicationResponse applyJob(Long jobId, Long studentId) {

        Job job = getJobEntity(jobId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        boolean alreadyApplied = applicationRepository.existsByJobIdAndStudentId(jobId, studentId);

        if (alreadyApplied) {
            throw new RuntimeException("Student already applied for this job");
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .student(student)
                .status(JobApplication.ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();

        JobApplication savedApplication = applicationRepository.save(application);

        job.setAppliedCount(job.getAppliedCount() + 1);
        jobRepository.save(job);

        return mapToApplicationResponse(savedApplication);
    }

    public List<JobApplicationResponse> getStudentApplications(Long studentId) {

        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return applicationRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToApplicationResponse)
                .toList();
    }

    public List<JobApplicationResponse> getJobApplications(Long jobId) {

        getJobEntity(jobId);

        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(this::mapToApplicationResponse)
                .toList();
    }

    public JobApplicationResponse cancelApplication(Long applicationId) {

        JobApplication application = getApplicationEntity(applicationId);

        if (application.getStatus() == JobApplication.ApplicationStatus.CANCELLED) {
            throw new RuntimeException("Application already cancelled");
        }

        application.setStatus(JobApplication.ApplicationStatus.CANCELLED);

        return mapToApplicationResponse(applicationRepository.save(application));
    }

    public JobApplicationResponse updateApplicationStatus(Long applicationId, String status) {

        JobApplication application = getApplicationEntity(applicationId);

        JobApplication.ApplicationStatus newStatus =
                JobApplication.ApplicationStatus.valueOf(status.toUpperCase());

        application.setStatus(newStatus);

        return mapToApplicationResponse(applicationRepository.save(application));
    }

    private Job getJobEntity(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    private JobApplication getApplicationEntity(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    private JobResponse mapToJobResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getAlumniId(),
                job.getTitle(),
                job.getCompany(),
                job.getDescription(),
                job.getLocation(),
                job.getJobType(),
                job.getSkillsRequired(),
                job.getAppliedCount()
        );
    }

    private JobApplicationResponse mapToApplicationResponse(JobApplication application) {

        Job job = application.getJob();
        Student student = application.getStudent();

        return new JobApplicationResponse(
                application.getId(),
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getJobType(),
                student.getId(),
                student.getName(),
                application.getStatus(),
                application.getAppliedAt()
        );
    }
}
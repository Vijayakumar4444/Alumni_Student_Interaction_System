package com.vijay.alumniportal.job.service;

import com.vijay.alumniportal.job.dto.JobRequest;
import com.vijay.alumniportal.job.dto.JobResponse;
import com.vijay.alumniportal.job.entity.Job;
import com.vijay.alumniportal.job.repository.JobRepository;
import com.vijay.alumniportal.student.entity.Student;
import com.vijay.alumniportal.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository repository;
    private final StudentRepository studentRepository;

    public JobService(JobRepository repository, StudentRepository studentRepository)
    {
        this.repository = repository;
        this.studentRepository = studentRepository;
    }

    public JobResponse createJob(JobRequest request) {
        Job job = Job.builder()
                .title(request.getTitle())
                .company(request.getCompany())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .skillsRequired(request.getSkillsRequired())
                .postedBy(request.getPostedBy())
                .appliedCount(0)
                .build();

        Job savedJob = repository.save(job);
        return mapToResponse(savedJob);
    }

    public List<JobResponse> getAllJobs() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobResponse getJobById(Long id) {
        Job job = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        return mapToResponse(job);
    }

    public JobResponse updateJob(Long id, JobRequest request) {
        Job job = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setPostedBy(request.getPostedBy());

        Job updatedJob = repository.save(job);
        return mapToResponse(updatedJob);
    }

    public void deleteJob(Long id) {
        Job job = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        repository.delete(job);
    }

    public JobResponse applyInternship(Long jobId, Long studentId) {
        Job job = repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job with id " + jobId + " does not exist"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student with id " + studentId + " does not exist"));

        if (job.getStudentsApplied().contains(student)) {
            throw new RuntimeException("Student already applied for this job");
        }

        job.getStudentsApplied().add(student);
        job.setAppliedCount(job.getStudentsApplied().size());

        Job updatedJob = repository.save(job);

        return mapToResponse(updatedJob);
    }

    private JobResponse mapToResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getDescription(),
                job.getLocation(),
                job.getJobType(),
                job.getSkillsRequired(),
                job.getPostedBy()
        );
    }
}
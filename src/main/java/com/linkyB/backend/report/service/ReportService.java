package com.linkyB.backend.report.service;

import com.linkyB.backend.report.converter.ReportConverter;
import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.report.repository.ReportRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.exception.UserNotFoundException;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportConverter reportConverter;

    // 유저 신고 등록
    @Transactional
    public ReportDto reportUser(long userGetReported, long userId, @Valid PostReportReq dto) {
        User Report = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User GetReported = userRepository.findById(userGetReported).orElseThrow(UserNotFoundException::new);

        Report entity = reportRepository.save(reportConverter.Report(Report, GetReported, dto));

        return ReportDto.of(entity);
    }
}

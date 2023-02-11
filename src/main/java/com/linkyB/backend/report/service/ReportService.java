package com.linkyB.backend.report.service;

import com.linkyB.backend.common.exception.LInkyBussinessException;
import com.linkyB.backend.report.converter.ReportConverter;
import com.linkyB.backend.report.dto.PostReportReq;
import com.linkyB.backend.report.dto.ReportDto;
import com.linkyB.backend.report.entity.Report;
import com.linkyB.backend.report.mapper.ReportMapper;
import com.linkyB.backend.report.repository.ReportRepository;
import com.linkyB.backend.user.domain.User;
import com.linkyB.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportConverter reportConverter;

    // 유저 신고 등록
    @Transactional
    public ReportDto reportUser(long userGetReported, long userId, PostReportReq dto) {
        User Report = userRepository.findById(userId)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        User GetReported = userRepository.findById(userGetReported)
                .orElseThrow(() -> new LInkyBussinessException("해당하는 유저가 없습니다.", HttpStatus.BAD_REQUEST));

        if (dto.getReportDetail().isEmpty()) {
            throw new LInkyBussinessException("신고 사유를 작성해주세요.", HttpStatus.BAD_REQUEST);
        }

        Report entity = reportRepository.save(reportConverter.Report(Report, GetReported, dto));
        ReportDto reportDto = ReportMapper.INSTANCE.entityToDto(entity);

        return reportDto;
    }
}

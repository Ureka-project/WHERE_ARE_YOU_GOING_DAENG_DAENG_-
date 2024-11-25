package com.daengdaeng_eodiga.project.visit.dto;

import java.time.LocalDate;
import java.util.List;

public record VisitResponse(LocalDate visitDate, List<PetsAtVisitTime> petsAtVisitTimes) {}

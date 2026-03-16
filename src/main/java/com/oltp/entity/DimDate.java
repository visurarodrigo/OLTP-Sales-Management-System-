package com.oltp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dim_date", indexes = {
        @Index(name = "idx_dim_date_full_date", columnList = "full_date"),
    @Index(name = "idx_dim_date_year_month", columnList = "year_num, month_num")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dim_date_id")
    private Long dimDateId;

    @Column(name = "full_date", nullable = false, unique = true)
    private LocalDate fullDate;

    @Column(name = "day_num", nullable = false)
    private Integer day;

    @Column(name = "month_num", nullable = false)
    private Integer month;

    @Column(name = "year_num", nullable = false)
    private Integer year;

    @Column(name = "quarter", nullable = false)
    private Integer quarter;

    @Column(name = "week_of_year", nullable = false)
    private Integer weekOfYear;
}

# Project Phases

This file tracks the project phases.

## Phase 1
- Name: OLTP created normal way (full project)
- Status: Completed
- Notes: Full normal OLTP project implementation is completed.

## Phase 2
- Name: Create dimensions and compare with normal version
- Status: Completed
- Notes:
	- Dimension model is created.
	- Comparison is done against the normal OLTP version.

## Phase 3
- Name: Introduce data warehouse architecture (staging + star schema + sales datamart)
- Status: Completed
- Notes:
	- Added staging table for sales extraction (`stage_sales`).
	- Rebuild pipeline now flows as OLTP -> Staging -> Star Schema (`dim_*`, `fact_sales`).
	- Added daily sales datamart aggregate table (`sales_datamart_daily`).
	- Added Phase 3 API endpoints to run each pipeline step independently.
	- Added pipeline status API for row counts and latest staging load timestamp.
	- Added reconciliation API to compare OLTP vs fact vs datamart totals by date range.
	- Added datamart analytics APIs for daily rows, top products, and top locations.
	- Added watermark-based incremental pipeline run from OLTP updates to staging.
	- Added persistent pipeline state table for latest successful source update timestamp.

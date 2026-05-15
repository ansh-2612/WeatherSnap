package com.example.weathersnap.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert
    suspend fun insertReport(report: ReportEntity)

    @Query("SELECT * FROM reports ORDER BY savedAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDraft(draft: DraftReportEntity)

    @Query("SELECT * FROM draft_report WHERE id = 1 LIMIT 1")
    suspend fun getDraftOnce(): DraftReportEntity?

    @Query("SELECT * FROM draft_report WHERE id = 1 LIMIT 1")
    fun observeDraft(): Flow<DraftReportEntity?>

    @Query("DELETE FROM draft_report WHERE id = 1")
    suspend fun clearDraft()
}


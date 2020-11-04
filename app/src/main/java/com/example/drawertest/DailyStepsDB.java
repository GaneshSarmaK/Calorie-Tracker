package com.example.drawertest;

import android.support.annotation.NonNull;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(primaryKeys = {"reportTime"})
public class DailyStepsDB {



        @NonNull
        @ColumnInfo(name = "reportTime")
        public String reportTime;

        @NonNull
        @ColumnInfo(name = "stepsRecord")
        public Integer stepsRecord;





        public DailyStepsDB(String reportTime, Integer stepsRecord) {
            this.reportTime=reportTime;
            this.stepsRecord=stepsRecord;
        }

        public Integer getStepsRecord() {
            return stepsRecord;
        }
        public String getReportTime(){
            return reportTime;
        }

        public void setStepsRecord(Integer stepsRecord) {
            this.stepsRecord = stepsRecord;
        }
        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }
    }
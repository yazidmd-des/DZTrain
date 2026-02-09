package com.yzd.dztrain

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// --- DATA MODELS ---
data class Stop(val name: String, val time: String)
data class Train(val id: String, val stops: List<Stop>)
data class SearchResult(val id: String, val depTime: String, val arrTime: String, val from: String, val to: String)

// --- THE COMPLETE DATASET (Extracted from Images) ---
object TrainRepository {

    // Helper to identify holiday-only trains
    fun isHolidayTrain(id: String): Boolean = id == "73-Holydays" || id == "75-Holydays" || id == "46-Holydays"

    val allTrains = listOf(
        // --- ALGER -> THENIA (Targeting your specific train numbers) ---
        Train("27", listOf(Stop("Alger", "06:30"), Stop("Agha", "06:33"), Stop("Ateliers", "06:36"), Stop("Hussein Dey", "06:40"), Stop("Caroubier", "06:43"), Stop("El Harrach", "06:46"), Stop("Oued Smar", "06:51"), Stop("Bab Ezzouar", "06:54"), Stop("Dar El Beida", "06:57"), Stop("Rouiba", "07:02"), Stop("Rouiba Ind", "07:04"), Stop("Reghaia Ind", "07:06"), Stop("Reghaia", "07:07"), Stop("Boudouaou", "07:13"), Stop("Corso", "07:16"), Stop("Boumerdes", "07:20"), Stop("Tidjelabine", "07:24"), Stop("Thenia", "07:29"))),
        // Morning El Affroun Connection
        Train("B152/153", listOf(Stop("Oued Smar", "7:00"), Stop("Bab Ezzouar", "7:03"), Stop("Dar El Beida", "7:06"), Stop("Rouiba", "7:12"), Stop("Rouiba Ind", "7:15"), Stop("Reghaia Ind", "7:16"), Stop("Reghaia", "7:18"), Stop("Boudouaou", "7:24"), Stop("Corso", "7:28"), Stop("Boumerdes", "7:32"), Stop("Tidjelabine", "7:36"), Stop("Thenia", "7:41"))),
        // Morning Zéralda Connection
        Train("B506/101", listOf(Stop("Oued Smar", "8:42"), Stop("Bab Ezzouar", "8:46"), Stop("Dar El Beida", "8:49"), Stop("Rouiba", "8:55"), Stop("Rouiba Ind", "8:58"), Stop("Reghaia Ind", "8:59"), Stop("Reghaia", "9:01"), Stop("Boudouaou", "9:07"), Stop("Corso", "9:10"), Stop("Boumerdes", "9:14"), Stop("Tidjelabine", "9:18"), Stop("Thenia", "9:23"))),
        // Afternoon Zéralda Connection (Short Route)
        Train("B508/103", listOf(Stop("Oued Smar", "13:11"), Stop("Bab Ezzouar", "13:14"), Stop("Dar El Beida", "13:17"), Stop("Rouiba", "13:23"), Stop("Rouiba Ind", "13:25"), Stop("Reghaia Ind", "13:26"), Stop("Reghaia", "13:30"))),
        // Afternoon El Affroun Connection
        Train("B152/153-PM", listOf(Stop("Oued Smar", "14:04"), Stop("Bab Ezzouar", "14:07"), Stop("Dar El Beida", "14:10"), Stop("Rouiba", "14:16"), Stop("Rouiba Ind", "14:19"), Stop("Reghaia Ind", "14:20"), Stop("Reghaia", "14:22"), Stop("Boudouaou", "14:28"), Stop("Corso", "14:32"), Stop("Boumerdes", "14:36"), Stop("Tidjelabine", "14:40"), Stop("Thenia", "14:45"))),
        // Evening Zéralda Connection
        Train("Zéralda-1720", listOf(Stop("Oued Smar", "17:20"), Stop("Bab Ezzouar", "17:23"), Stop("Dar El Beida", "17:26"), Stop("Rouiba", "17:32"), Stop("Rouiba Ind", "17:35"), Stop("Reghaia Ind", "17:36"), Stop("Reghaia", "17:38"), Stop("Boudouaou", "17:44"), Stop("Corso", "17:47"), Stop("Boumerdes", "17:51"), Stop("Tidjelabine", "17:57"), Stop("Thenia", "18:02"))),
        Train("33", listOf(Stop("Alger", "07:30"), Stop("Agha", "07:33"), Stop("Ateliers", "07:37"), Stop("Hussein Dey", "07:41"), Stop("Caroubier", "07:44"), Stop("El Harrach", "07:47"), Stop("Oued Smar", "07:53"), Stop("Bab Ezzouar", "07:56"), Stop("Dar El Beida", "08:00"), Stop("Rouiba", "08:05"), Stop("Rouiba Ind", "08:07"), Stop("Reghaia Ind", "08:09"), Stop("Reghaia", "08:10"), Stop("Boudouaou", "08:13"), Stop("Corso", "08:16"), Stop("Boumerdes", "08:20"), Stop("Tidjelabine", "08:24"), Stop("Thenia", "08:29"))),
        Train("35", listOf(Stop("Alger", "08:30"), Stop("Agha", "08:33"), Stop("Ateliers", "08:36"), Stop("Hussein Dey", "08:40"), Stop("Caroubier", "08:43"), Stop("El Harrach", "08:46"), Stop("Oued Smar", "08:51"), Stop("Bab Ezzouar", "08:54"), Stop("Dar El Beida", "08:57"), Stop("Rouiba", "09:02"), Stop("Rouiba Ind", "09:04"), Stop("Reghaia Ind", "09:06"), Stop("Reghaia", "09:07"), Stop("Boudouaou", "09:13"), Stop("Corso", "09:16"), Stop("Boumerdes", "09:20"), Stop("Tidjelabine", "09:24"), Stop("Thenia", "09:29"))),
        Train("41", listOf(Stop("Alger", "09:45"), Stop("Agha", "09:48"), Stop("Ateliers", "09:51"), Stop("Hussein Dey", "09:55"), Stop("Caroubier", "09:58"), Stop("El Harrach", "10:01"), Stop("Oued Smar", "10:06"), Stop("Bab Ezzouar", "10:09"), Stop("Dar El Beida", "10:12"), Stop("Rouiba", "10:17"), Stop("Reghaia", "10:22"), Stop("Boudouaou", "10:28"), Stop("Corso", "10:31"), Stop("Boumerdes", "10:35"), Stop("Tidjelabine", "10:39"), Stop("Thenia", "10:44"))),
        Train("47", listOf(Stop("Alger", "11:20"), Stop("Agha", "11:23"), Stop("Ateliers", "11:26"), Stop("Hussein Dey", "11:30"), Stop("Caroubier", "11:33"), Stop("El Harrach", "11:36"), Stop("Oued Smar", "11:41"), Stop("Bab Ezzouar", "11:44"), Stop("Dar El Beida", "11:47"), Stop("Rouiba", "11:52"), Stop("Rouiba Ind", "11:54"), Stop("Reghaia", "11:57"), Stop("Boudouaou", "12:03"), Stop("Corso", "12:06"), Stop("Boumerdes", "12:10"), Stop("Tidjelabine", "12:14"), Stop("Thenia", "12:19"))),
        Train("51", listOf(Stop("Alger", "12:35"), Stop("Agha", "12:38"), Stop("Ateliers", "12:42"), Stop("Hussein Dey", "12:46"), Stop("Caroubier", "12:49"), Stop("El Harrach", "12:52"), Stop("Oued Smar", "12:58"), Stop("Bab Ezzouar", "13:01"), Stop("Dar El Beida", "13:05"), Stop("Rouiba", "13:10"), Stop("Rouiba Ind", "13:12"), Stop("Reghaia Ind", "13:14"), Stop("Reghaia", "13:15"), Stop("Boudouaou", "13:21"), Stop("Corso", "13:24"), Stop("Boumerdes", "13:28"), Stop("Tidjelabine", "13:32"), Stop("Thenia", "13:37"))),
        Train("57", listOf(Stop("Alger", "14:00"), Stop("Agha", "14:03"), Stop("Ateliers", "14:06"), Stop("Hussein Dey", "14:10"), Stop("Caroubier", "14:13"), Stop("El Harrach", "14:16"), Stop("Oued Smar", "14:21"), Stop("Bab Ezzouar", "14:24"), Stop("Dar El Beida", "14:27"), Stop("Rouiba", "14:32"), Stop("Reghaia", "14:37"), Stop("Boudouaou", "14:43"), Stop("Corso", "14:46"), Stop("Boumerdes", "14:50"), Stop("Tidjelabine", "14:54"), Stop("Thenia", "14:59"))),
        Train("61", listOf(Stop("Alger", "15:05"), Stop("Agha", "15:08"), Stop("Ateliers", "15:12"), Stop("Hussein Dey", "15:16"), Stop("Caroubier", "15:19"), Stop("El Harrach", "15:22"), Stop("Oued Smar", "15:28"), Stop("Bab Ezzouar", "15:31"), Stop("Dar El Beida", "15:35"), Stop("Rouiba", "15:40"), Stop("Rouiba Ind", "15:42"), Stop("Reghaia Ind", "15:44"), Stop("Reghaia", "15:45"), Stop("Boudouaou", "15:51"), Stop("Corso", "15:54"), Stop("Boumerdes", "15:58"), Stop("Tidjelabine", "16:02"), Stop("Thenia", "16:07"))),
        Train("67", listOf(Stop("Alger", "16:05"), Stop("Agha", "16:08"), Stop("Ateliers", "16:11"), Stop("Hussein Dey", "16:15"), Stop("Caroubier", "16:18"), Stop("El Harrach", "16:21"), Stop("Oued Smar", "16:26"), Stop("Bab Ezzouar", "16:29"), Stop("Dar El Beida", "16:32"), Stop("Rouiba", "16:37"), Stop("Rouiba Ind", "16:39"), Stop("Reghaia Ind", "16:41"), Stop("Reghaia", "16:42"), Stop("Boudouaou", "16:48"), Stop("Corso", "16:51"), Stop("Boumerdes", "16:55"), Stop("Tidjelabine", "16:59"), Stop("Thenia", "17:04"))),
        Train("119", listOf(Stop("Agha", "16:21"), Stop("El Harrach", "16:31"), Stop("Dar El Beida", "16:40"), Stop("Rouiba", "16:44"), Stop("Reghaia", "16:49"), Stop("Boumerdes", "17:00"), Stop("Thenia", "17:09"))),
        Train("15", listOf(Stop("Agha", "16:48"), Stop("El Harrach", "16:59"), Stop("Dar El Beida", "17:08"), Stop("Rouiba", "17:14"), Stop("Reghaia", "17:21"), Stop("Boumerdes", "17:33"), Stop("Thenia", "17:43"))),
        Train("71", listOf(Stop("Alger", "17:05"), Stop("Agha", "17:08"), Stop("Ateliers", "17:12"), Stop("Hussein Dey", "17:16"), Stop("Caroubier", "17:19"), Stop("El Harrach", "17:22"), Stop("Oued Smar", "17:28"), Stop("Bab Ezzouar", "17:31"), Stop("Dar El Beida", "17:36"), Stop("Rouiba", "17:40"), Stop("Rouiba Ind", "17:42"), Stop("Reghaia Ind", "17:44"), Stop("Reghaia", "17:45"), Stop("Boudouaou", "17:51"), Stop("Corso", "17:54"), Stop("Boumerdes", "17:58"), Stop("Tidjelabine", "18:02"), Stop("Thenia", "18:07"))),
        Train("121", listOf(Stop("Agha", "17:33"), Stop("El Harrach", "17:44"), Stop("Dar El Beida", "17:53"), Stop("Rouiba", "17:57"), Stop("Reghaia", "18:02"), Stop("Boumerdes", "18:13"), Stop("Thenia", "18:21"))),
        Train("73-Holydays", listOf(Stop("Alger", "17:40"), Stop("Agha", "17:43"), Stop("Ateliers", "17:46"), Stop("Hussein Dey", "17:50"), Stop("Caroubier", "17:53"), Stop("El Harrach", "17:56"), Stop("Oued Smar", "18:02"), Stop("Bab Ezzouar", "18:05"), Stop("Dar El Beida", "18:09"), Stop("Rouiba", "18:14"), Stop("Rouiba Ind", "18:16"), Stop("Reghaia Ind", "18:18"), Stop("Reghaia", "18:19"), Stop("Boudouaou", "18:25"), Stop("Corso", "18:28"), Stop("Boumerdes", "18:32"), Stop("Tidjelabine", "18:36"), Stop("Thenia", "18:41"))),
        Train("75", listOf(Stop("Alger", "18:05"), Stop("Agha", "18:08"), Stop("Ateliers", "18:12"), Stop("Hussein Dey", "18:16"), Stop("Caroubier", "18:19"), Stop("El Harrach", "18:22"), Stop("Oued Smar", "18:28"), Stop("Bab Ezzouar", "18:31"), Stop("Dar El Beida", "18:35"), Stop("Rouiba", "18:40"), Stop("Reghaia", "18:45"), Stop("Boudouaou", "18:51"), Stop("Corso", "18:54"), Stop("Boumerdes", "18:58"), Stop("Tidjelabine", "19:02"), Stop("Thenia", "19:07"))),
        Train("75-Holydays", listOf(Stop("Alger", "18:30"), Stop("Agha", "18:33"), Stop("Ateliers", "18:36"), Stop("Hussein Dey", "18:40"), Stop("Caroubier", "18:43"), Stop("El Harrach", "18:46"), Stop("Oued Smar", "18:51"), Stop("Bab Ezzouar", "18:54"), Stop("Dar El Beida", "18:57"), Stop("Rouiba", "19:02"), Stop("Rouiba Ind", "19:04"), Stop("Reghaia Ind", "19:06"), Stop("Reghaia", "19:07"), Stop("Boudouaou", "19:13"), Stop("Corso", "19:16"), Stop("Boumerdes", "19:20"), Stop("Tidjelabine", "19:24"), Stop("Thenia", "19:29"))),
        Train("79", listOf(Stop("Alger", "19:00"), Stop("Agha", "19:03"), Stop("Ateliers", "19:07"), Stop("Hussein Dey", "19:11"), Stop("Caroubier", "19:14"), Stop("El Harrach", "19:17"), Stop("Oued Smar", "19:23"), Stop("Bab Ezzouar", "19:26"), Stop("Dar El Beida", "19:30"), Stop("Rouiba", "19:35"), Stop("Reghaia", "19:40"), Stop("Boudouaou", "19:46"), Stop("Corso", "19:49"), Stop("Boumerdes", "19:53"), Stop("Tidjelabine", "19:57"), Stop("Thenia", "20:02"))),

        // Holiday Only logic handles visibility

        // Add 35, 41, 47, 51, 57, 61, 67, 71, 121, 79 following the dash-rule...

        // --- THENIA -> ALGER (Targeting your specific train numbers) ---

        Train("B100/505 Zéralda", listOf(Stop("Thenia", "5:55"), Stop("Tidjelabine", "6:00"), Stop("Boumerdes", "6:04"), Stop("Corso", "6:10"), Stop("Boudouaou", "6:14"), Stop("Reghaia", "6:19"), Stop("Reghaia Ind", "6:22"), Stop("Rouiba Ind", "6:23"), Stop("Rouiba", "6:25"), Stop("Dar El Beida", "6:31"), Stop("Bab Ezzouar", "6:35"), Stop("Oued Smar", "6:37"))),
        Train("22", listOf(Stop("Thenia", "06:00"), Stop("Tidjelabine", "06:04"), Stop("Boumerdes", "06:08"), Stop("Corso", "06:12"), Stop("Boudouaou", "06:15"), Stop("Reghaia", "06:21"), Stop("Reghaia Ind", "06:23"), Stop("Rouiba Ind", "06:25"), Stop("Rouiba", "06:26"), Stop("Dar El Beida", "06:32"), Stop("Bab Ezzouar", "06:35"), Stop("Oued Smar", "06:39"), Stop("El Harrach", "06:44"), Stop("Caroubier", "06:47"), Stop("Hussein Dey", "06:50"), Stop("Ateliers", "06:54"), Stop("Agha", "06:57"), Stop("Alger", "07:02"))),
        Train("B114", listOf(Stop("Thenia", "06:21"), Stop("Boumerdes", "06:29"), Stop("Reghaia", "06:39"), Stop("Rouiba", "06:45"), Stop("Dar El Beida", "06:51"), Stop("El Harrach", "07:00"), Stop("Agha", "07:10"))),
        Train("28", listOf(Stop("Thenia", "06:45"), Stop("Tidjelabine", "06:49"), Stop("Boumerdes", "06:53"), Stop("Corso", "06:58"), Stop("Boudouaou", "07:01"), Stop("Reghaia", "07:07"), Stop("Reghaia Ind", "07:09"), Stop("Rouiba Ind", "07:11"), Stop("Rouiba", "07:12"), Stop("Dar El Beida", "07:18"), Stop("Bab Ezzouar", "07:21"), Stop("Oued Smar", "07:24"), Stop("El Harrach", "07:29"), Stop("Caroubier", "07:32"), Stop("Hussein Dey", "07:35"), Stop("Ateliers", "07:39"), Stop("Agha", "07:42"), Stop("Alger", "07:47"))),
        Train("12", listOf(Stop("Thenia", "07:00"), Stop("Boumerdes", "07:08"), Stop("Reghaia", "07:18"), Stop("Rouiba", "07:24"), Stop("Dar El Beida", "07:30"), Stop("El Harrach", "07:38"), Stop("Agha", "07:48"))),
        Train("104", listOf(Stop("Thenia", "07:30"), Stop("Boumerdes", "07:40"), Stop("Reghaia", "07:50"), Stop("Rouiba", "07:56"), Stop("Dar El Beida", "08:02"), Stop("El Harrach", "08:10"), Stop("Agha", "08:20"))),
        Train("34", listOf(Stop("Thenia", "08:05"), Stop("Tidjelabine", "08:09"), Stop("Boumerdes", "08:13"), Stop("Corso", "08:17"), Stop("Boudouaou", "08:20"), Stop("Reghaia", "08:26"), Stop("Reghaia Ind", "08:28"), Stop("Rouiba Ind", "08:30"), Stop("Rouiba", "08:31"), Stop("Dar El Beida", "08:37"), Stop("Bab Ezzouar", "08:40"), Stop("Oued Smar", "08:43"), Stop("El Harrach", "08:48"), Stop("Caroubier", "08:51"), Stop("Hussein Dey", "08:54"), Stop("Ateliers", "08:58"), Stop("Agha", "09:01"), Stop("Alger", "09:05"))),
        Train("B126/127 El Affroun", listOf(Stop("Thenia", "8:35"), Stop("Tidjelabine", "8:40"), Stop("Boumerdes", "8:45"), Stop("Corso", "8:50"), Stop("Boudouaou", "8:55"), Stop("Reghaia", "9:00"), Stop("Reghaia Ind", "9:03"), Stop("Rouiba Ind", "9:04"), Stop("Rouiba", "9:06"), Stop("Dar El Beida", "9:12"), Stop("Bab Ezzouar", "9:15"), Stop("Oued Smar", "9:19"))),
        Train("40", listOf(Stop("Thenia", "09:25"), Stop("Tidjelabine", "09:29"), Stop("Boumerdes", "09:33"), Stop("Corso", "09:37"), Stop("Boudouaou", "09:40"), Stop("Reghaia", "09:46"), Stop("Rouiba", "09:51"), Stop("Dar El Beida", "09:57"), Stop("Bab Ezzouar", "10:00"), Stop("Oued Smar", "10:03"), Stop("El Harrach", "10:08"), Stop("Caroubier", "10:12"), Stop("Hussein Dey", "10:15"), Stop("Ateliers", "10:19"), Stop("Agha", "10:22"), Stop("Alger", "10:27"))),
        Train("B102/507 Zéralda", listOf(Stop("Thenia", "10:20"), Stop("Tidjelabine", "10:25"), Stop("Boumerdes", "10:30"), Stop("Corso", "10:35"), Stop("Boudouaou", "10:40"), Stop("Reghaia", "10:44"), Stop("Reghaia Ind", "10:47"), Stop("Rouiba Ind", "10:48"), Stop("Rouiba", "10:50"), Stop("Dar El Beida", "10:54"), Stop("Bab Ezzouar", "10:57"), Stop("Oued Smar", "11:01"))),
        Train("44", listOf(Stop("Thenia", "10:35"), Stop("Tidjelabine", "10:39"), Stop("Boumerdes", "10:43"), Stop("Corso", "10:47"), Stop("Boudouaou", "10:50"), Stop("Reghaia", "10:56"), Stop("Rouiba", "11:01"), Stop("Dar El Beida", "11:07"), Stop("Bab Ezzouar", "11:10"), Stop("Oued Smar", "11:13"), Stop("El Harrach", "11:23"), Stop("Caroubier", "11:27"), Stop("Hussein Dey", "11:30"), Stop("Ateliers", "11:34"), Stop("Agha", "11:37"), Stop("Alger", "11:42"))),
        Train("46-Holydays", listOf(Stop("Thenia", "11:00"), Stop("Tidjelabine", "11:04"), Stop("Boumerdes", "11:08"), Stop("Corso", "11:12"), Stop("Boudouaou", "11:15"), Stop("Reghaia", "11:21"), Stop("Rouiba", "11:26"), Stop("Dar El Beida", "11:32"), Stop("Bab Ezzouar", "11:35"), Stop("Oued Smar", "11:39"), Stop("El Harrach", "11:44"), Stop("Caroubier", "11:47"), Stop("Hussein Dey", "11:50"), Stop("Ateliers", "11:54"), Stop("Agha", "11:57"), Stop("Alger", "12:02"))),
        Train("50", listOf(Stop("Thenia", "12:00"), Stop("Tidjelabine", "12:04"), Stop("Boumerdes", "12:08"), Stop("Corso", "12:12"), Stop("Boudouaou", "12:15"), Stop("Reghaia", "12:21"), Stop("Rouiba", "12:26"), Stop("Dar El Beida", "12:32"), Stop("Bab Ezzouar", "12:35"), Stop("Oued Smar", "12:39"), Stop("El Harrach", "12:44"), Stop("Caroubier", "12:48"), Stop("Hussein Dey", "12:51"), Stop("Ateliers", "12:55"), Stop("Agha", "12:58"), Stop("Alger", "13:03"))),
        Train("56", listOf(Stop("Thenia", "13:30"), Stop("Tidjelabine", "13:34"), Stop("Boumerdes", "13:38"), Stop("Corso", "13:42"), Stop("Boudouaou", "13:45"), Stop("Reghaia", "13:51"), Stop("Rouiba", "13:56"), Stop("Dar El Beida", "14:02"), Stop("Bab Ezzouar", "14:05"), Stop("Oued Smar", "14:08"), Stop("El Harrach", "14:13"), Stop("Caroubier", "14:17"), Stop("Hussein Dey", "14:20"), Stop("Ateliers", "14:24"), Stop("Agha", "14:27"), Stop("Alger", "14:32"))),
        Train("B124/125", listOf(Stop("Reghaia", "14:40"), Stop("Reghaia Ind", "14:42"), Stop("Rouiba Ind", "14:43"), Stop("Rouiba", "14:45"), Stop("Dar El Beida", "14:50"), Stop("Bab Ezzouar", "14:53"), Stop("Oued Smar", "14:56"))),
        Train("58", listOf(Stop("Thenia", "14:20"), Stop("Tidjelabine", "14:24"), Stop("Boumerdes", "14:28"), Stop("Corso", "14:32"), Stop("Boudouaou", "14:35"), Stop("Reghaia", "14:41"), Stop("Rouiba", "14:46"), Stop("Dar El Beida", "14:52"), Stop("Bab Ezzouar", "14:55"), Stop("Oued Smar", "14:58"), Stop("El Harrach", "15:03"), Stop("Caroubier", "15:07"), Stop("Hussein Dey", "15:10"), Stop("Ateliers", "15:14"), Stop("Agha", "15:17"), Stop("Alger", "15:20"))),
        Train("62", listOf(Stop("Thenia", "15:35"), Stop("Tidjelabine", "15:39"), Stop("Boumerdes", "15:43"), Stop("Corso", "15:47"), Stop("Boudouaou", "15:50"), Stop("Reghaia", "15:56"), Stop("Reghaia Ind", "15:58"), Stop("Rouiba Ind", "16:00"), Stop("Rouiba", "16:01"), Stop("Dar El Beida", "16:07"), Stop("Bab Ezzouar", "16:10"), Stop("Oued Smar", "16:13"), Stop("El Harrach", "16:23"), Stop("Caroubier", "16:27"), Stop("Hussein Dey", "16:30"), Stop("Ateliers", "16:34"), Stop("Agha", "16:37"), Stop("Alger", "16:42"))),
        Train("B128/129", listOf(Stop("Thenia", "16:00"), Stop("Tidjelabine", "16:05"), Stop("Boumerdes", "16:10"), Stop("Corso", "16:15"), Stop("Boudouaou", "16:20"), Stop("Reghaia", "16:25"), Stop("Reghaia Ind", "16:27"), Stop("Rouiba Ind", "16:29"), Stop("Rouiba", "16:31"), Stop("Dar El Beida", "16:37"), Stop("Bab Ezzouar", "16:40"), Stop("Oued Smar", "16:44"))),
        Train("66", listOf(Stop("Thenia", "16:30"), Stop("Tidjelabine", "16:34"), Stop("Boumerdes", "16:38"), Stop("Corso", "16:42"), Stop("Boudouaou", "16:45"), Stop("Reghaia", "16:51"), Stop("Reghaia Ind", "16:53"), Stop("Rouiba Ind", "16:55"), Stop("Rouiba", "16:56"), Stop("Dar El Beida", "17:02"), Stop("Bab Ezzouar", "17:05"), Stop("Oued Smar", "17:08"), Stop("El Harrach", "17:13"), Stop("Caroubier", "17:17"), Stop("Hussein Dey", "17:20"), Stop("Ateliers", "17:24"), Stop("Agha", "17:27"), Stop("Alger", "17:32"))),
        Train("74", listOf(Stop("Thenia", "17:45"), Stop("Tidjelabine", "17:49"), Stop("Boumerdes", "17:53"), Stop("Corso", "17:57"), Stop("Boudouaou", "18:00"), Stop("Reghaia", "18:06"), Stop("Rouiba", "18:11"), Stop("Dar El Beida", "18:17"), Stop("Bab Ezzouar", "18:20"), Stop("Oued Smar", "18:23"), Stop("El Harrach", "18:28"), Stop("Caroubier", "18:32"), Stop("Hussein Dey", "18:35"), Stop("Ateliers", "18:39"), Stop("Agha", "18:42"), Stop("Alger", "18:46"))),
        Train("78", listOf(Stop("Thenia", "18:50"), Stop("Tidjelabine", "18:54"), Stop("Boumerdes", "18:58"), Stop("Corso", "19:02"), Stop("Boudouaou", "19:05"), Stop("Reghaia", "19:11"), Stop("Rouiba", "19:16"), Stop("Dar El Beida", "19:22"), Stop("Bab Ezzouar", "19:25"), Stop("Oued Smar", "19:28"), Stop("El Harrach", "19:33"), Stop("Caroubier", "19:36"), Stop("Hussein Dey", "19:39"), Stop("Ateliers", "19:43"), Stop("Agha", "19:46"), Stop("Alger", "19:51"))),

        // Add 22, 12, 104, 34, 40, 50, 56, 58, 62, 66, 78 following the dash-rule...
    )

    val stations = listOf(
        "Alger", "Agha", "Ateliers", "Hussein Dey", "Caroubier", "El Harrach",
        "Oued Smar", "Bab Ezzouar", "Dar El Beida", "Rouiba", "Rouiba ZI",
        "Reghaia ZI", "Reghaia", "Boudouaou", "Corso", "Boumerdes", "Tidjelabine",
        "Thenia", "Si Mustapha", "Issers", "Bordj Menaiel", "Naceria",
        "Tadmait", "Draâ Ben Khedda", "Tizi Ouzou", "Boukhalfa", "Oued Aissi"
    ).sorted()
}

class MainActivity : AppCompatActivity() {

    private var selectedTime = "00:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startInput = findViewById<AutoCompleteTextView>(R.id.startStation)
        val targetInput = findViewById<AutoCompleteTextView>(R.id.targetStation)
        val btnTime = findViewById<Button>(R.id.btnTime)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnSwap = findViewById<Button>(R.id.btnSwap)
        val tvNoResults = findViewById<TextView>(R.id.tvNoResults)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Setup Autocomplete
        val stationAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, TrainRepository.stations)
        startInput.setAdapter(stationAdapter)
        targetInput.setAdapter(stationAdapter)

        // Swap Feature
        btnSwap.setOnClickListener {
            val temp = startInput.text.toString()
            startInput.setText(targetInput.text.toString())
            targetInput.setText(temp)
        }

        // Time Picker
        btnTime.setOnClickListener {
            TimePickerDialog(this, { _, h, m ->
                selectedTime = String.format("%02d:%02d", h, m)
                btnTime.text = "Starting from $selectedTime"
            }, 8, 0, true).show()
        }

        // Search Logic
        btnSearch.setOnClickListener {
            val start = startInput.text.toString().trim()
            val target = targetInput.text.toString().trim()

            val filtered = TrainRepository.allTrains.mapNotNull { train ->
                val sIdx = train.stops.indexOfFirst { it.name.equals(start, true) }
                val tIdx = train.stops.indexOfFirst { it.name.equals(target, true) }

                if (sIdx != -1 && tIdx != -1 && tIdx > sIdx) {
                    val dep = train.stops[sIdx].time
                    if (dep >= selectedTime) {
                        SearchResult(train.id, dep, train.stops[tIdx].time, start, target)
                    } else null
                } else null
            }.sortedBy { it.depTime }

            if (filtered.isEmpty()) {
                tvNoResults.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                tvNoResults.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = TrainAdapter(filtered) { shareTrainDetails(it) }
            }
        }
    }

    private fun shareTrainDetails(result: SearchResult) {
        val message = "🚆 Train Info: ${result.from} -> ${result.to}\n" +
                "Train: ${result.id}\nDeparture: ${result.depTime}\nArrival: ${result.arrTime}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(Intent.createChooser(intent, "Share Schedule"))
    }
}

// --- RECYCLERVIEW ADAPTER ---
class TrainAdapter(private val list: List<SearchResult>, private val onLongClick: (SearchResult) -> Unit) :
    RecyclerView.Adapter<TrainAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val txt: TextView = v.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.txt.text = "Train ${item.id}\nDep: ${item.depTime}  ---  Arr: ${item.arrTime}"
        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            Toast.makeText(holder.itemView.context, "Sharing details...", Toast.LENGTH_SHORT).show()
            true
        }
    }
    override fun getItemCount() = list.size
}
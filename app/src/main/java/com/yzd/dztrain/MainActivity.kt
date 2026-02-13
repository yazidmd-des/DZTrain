package com.yzd.dztrain

import android.app.TimePickerDialog
import android.content.Context
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// --- DATA MODELS ---
data class Stop(val name: String, val time: String)
data class Train(val id: String, val stops: List<Stop>)
data class SearchResult(
    val id: String,
    val depTime: String,
    val arrTime: String,
    val from: String,
    val to: String,
    val stops: List<Stop>
)

data class FavoriteRoute(val from: String, val to: String, val time: String)

// --- THE COMPLETE DATASET (Extracted from Images) ---
object TrainRepository {

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
        Train("B154/155", listOf(Stop("Oued Smar", "14:04"), Stop("Bab Ezzouar", "14:07"), Stop("Dar El Beida", "14:10"), Stop("Rouiba", "14:16"), Stop("Rouiba Ind", "14:19"), Stop("Reghaia Ind", "14:20"), Stop("Reghaia", "14:22"), Stop("Boudouaou", "14:28"), Stop("Corso", "14:32"), Stop("Boumerdes", "14:36"), Stop("Tidjelabine", "14:40"), Stop("Thenia", "14:45"))),
        // Evening Zéralda Connection
        Train("B510/105", listOf(Stop("Oued Smar", "17:20"), Stop("Bab Ezzouar", "17:23"), Stop("Dar El Beida", "17:26"), Stop("Rouiba", "17:32"), Stop("Rouiba Ind", "17:35"), Stop("Reghaia Ind", "17:36"), Stop("Reghaia", "17:38"), Stop("Boudouaou", "17:44"), Stop("Corso", "17:47"), Stop("Boumerdes", "17:51"), Stop("Tidjelabine", "17:57"), Stop("Thenia", "18:02"))),
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
        Train("73-H", listOf(Stop("Alger", "17:40"), Stop("Agha", "17:43"), Stop("Ateliers", "17:46"), Stop("Hussein Dey", "17:50"), Stop("Caroubier", "17:53"), Stop("El Harrach", "17:56"), Stop("Oued Smar", "18:02"), Stop("Bab Ezzouar", "18:05"), Stop("Dar El Beida", "18:09"), Stop("Rouiba", "18:14"), Stop("Rouiba Ind", "18:16"), Stop("Reghaia Ind", "18:18"), Stop("Reghaia", "18:19"), Stop("Boudouaou", "18:25"), Stop("Corso", "18:28"), Stop("Boumerdes", "18:32"), Stop("Tidjelabine", "18:36"), Stop("Thenia", "18:41"))),
        Train("75", listOf(Stop("Alger", "18:05"), Stop("Agha", "18:08"), Stop("Ateliers", "18:12"), Stop("Hussein Dey", "18:16"), Stop("Caroubier", "18:19"), Stop("El Harrach", "18:22"), Stop("Oued Smar", "18:28"), Stop("Bab Ezzouar", "18:31"), Stop("Dar El Beida", "18:35"), Stop("Rouiba", "18:40"), Stop("Reghaia", "18:45"), Stop("Boudouaou", "18:51"), Stop("Corso", "18:54"), Stop("Boumerdes", "18:58"), Stop("Tidjelabine", "19:02"), Stop("Thenia", "19:07"))),
        Train("75-H", listOf(Stop("Alger", "18:30"), Stop("Agha", "18:33"), Stop("Ateliers", "18:36"), Stop("Hussein Dey", "18:40"), Stop("Caroubier", "18:43"), Stop("El Harrach", "18:46"), Stop("Oued Smar", "18:51"), Stop("Bab Ezzouar", "18:54"), Stop("Dar El Beida", "18:57"), Stop("Rouiba", "19:02"), Stop("Rouiba Ind", "19:04"), Stop("Reghaia Ind", "19:06"), Stop("Reghaia", "19:07"), Stop("Boudouaou", "19:13"), Stop("Corso", "19:16"), Stop("Boumerdes", "19:20"), Stop("Tidjelabine", "19:24"), Stop("Thenia", "19:29"))),
        Train("79", listOf(Stop("Alger", "19:00"), Stop("Agha", "19:03"), Stop("Ateliers", "19:07"), Stop("Hussein Dey", "19:11"), Stop("Caroubier", "19:14"), Stop("El Harrach", "19:17"), Stop("Oued Smar", "19:23"), Stop("Bab Ezzouar", "19:26"), Stop("Dar El Beida", "19:30"), Stop("Rouiba", "19:35"), Stop("Reghaia", "19:40"), Stop("Boudouaou", "19:46"), Stop("Corso", "19:49"), Stop("Boumerdes", "19:53"), Stop("Tidjelabine", "19:57"), Stop("Thenia", "20:02"))),

        // --- THENIA -> OUED AISSI

        Train("103", listOf(
            Stop("Thenia", "05:15"), Stop("Si Mustapha", "05:19"), Stop("Issers", "05:22"),
            Stop("Bordj Menaiel", "05:26"), Stop("Naceria", "05:33"), Stop("Tadmait", "05:38"),
            Stop("Draâ Ben Khedda", "05:42"), Stop("Boukhalfa", "05:47"), Stop("Tizi Ouzou", "05:50"),
            Stop("Kef Naadja", "06:01"), Stop("Oued Aissi -Université", "06:06"), Stop("Oued Aissi", "06:09")
        )),
        Train("B133", listOf(
            Stop("Thenia", "06:25"), Stop("Si Mustapha", "06:29"), Stop("Issers", "06:36"),
            Stop("Bordj Menaiel", "06:41"), Stop("Naceria", "06:48"), Stop("Tadmait", "06:54"),
            Stop("Draâ Ben Khedda", "07:09"), Stop("Boukhalfa", "07:13"), Stop("Tizi Ouzou", "07:17"),
            Stop("Kef Naadja", "07:21"), Stop("Oued Aissi -Université", "07:26"), Stop("Oued Aissi", "07:29")
        )),
        Train("105", listOf(
            Stop("Thenia", "07:32"), Stop("Si Mustapha", "07:36"), Stop("Issers", "07:40"),
            Stop("Bordj Menaiel", "07:45"), Stop("Naceria", "07:52"), Stop("Tadmait", "07:58"),
            Stop("Draâ Ben Khedda", "08:03"), Stop("Boukhalfa", "08:07"), Stop("Tizi Ouzou", "08:11"),
            Stop("Kef Naadja", "08:15"), Stop("Oued Aissi -Université", "08:20"), Stop("Oued Aissi", "08:23")
        )),

        // --- AGHA -> OUED AISSI

        Train("119", listOf(
            Stop("Agha", "16:24"), Stop("El Harrach", "16:31"),
            Stop("Dar El Beida", "16:40"), Stop("Rouiba", "16:44"),
            Stop("Reghaia", "16:49"), Stop("Boumerdes", "17:00"),
            Stop("Thenia", "17:09"), Stop("Si Mustapha", "17:16"),
            Stop("Les Issers", "17:20"), Stop("Bordj Menaiel", "17:25"),
            Stop("Naciria", "17:32"), Stop("Tadmait", "17:38"),
            Stop("Draa Ben Khedda", "17:43"), Stop("Boukhalfa", "17:47"),
            Stop("Tizi Ouzou", "17:51"), Stop("Kef Naadja", "17:55"),
            Stop("Oued Aissi - University", "18:00"), Stop("Oued Aissi", "18:03")
        )),

                Train("121", listOf(
            Stop("Agha", "17:35"), Stop("El Harrach", "17:44"),
            Stop("Dar El Beida", "17:53"), Stop("Rouiba", "17:57"),
            Stop("Reghaia", "18:02"), Stop("Boumerdes", "18:13"),
            Stop("Thenia", "18:21"), Stop("Si Mustapha", "18:26"),
            Stop("Les Issers", "18:30"), Stop("Bordj Menaiel", "18:35"),
            Stop("Naciria", "18:42"), Stop("Tadmait", "18:48"),
            Stop("Draa Ben Khedda", "18:53"), Stop("Boukhalfa", "18:57"),
            Stop("Tizi Ouzou", "19:01"), Stop("Kef Naadja", "19:05"),
            Stop("Oued Aissi - University", "19:10"), Stop("Oued Aissi", "19:13")
        )),

        // --- OUED AISSI -> AGHA

        Train("B114", listOf(
            Stop("Oued Aissi", "05:30"), Stop("Oued Aissi - University", "05:32"),
            Stop("Kef Naadja", "05:37"), Stop("Tizi Ouzou", "05:41"),
            Stop("Boukhalfa", "05:45"), Stop("Draa Ben Khedda", "05:49"),
            Stop("Tadmait", "05:54"), Stop("Naciria", "06:00"),
            Stop("Bordj Menaiel", "06:07"), Stop("Les Issers", "06:13"),
            Stop("Si Mustapha", "06:17"), Stop("Thenia", "06:21"),
            Stop("Boumerdes", "06:29"), Stop("Reghaia", "06:39"),
            Stop("Rouiba", "06:45"), Stop("Dar El Beida", "06:51"),
            Stop("El Harrach", "07:00"), Stop("Agha", "07:10")
        )),

        Train("104", listOf(
            Stop("Oued Aissi", "06:40"), Stop("Oued Aissi - University", "06:42"),
            Stop("Kef Naadja", "06:47"), Stop("Tizi Ouzou", "06:51"),
            Stop("Boukhalfa", "06:55"), Stop("Draa Ben Khedda", "06:58"),
            Stop("Tadmait", "07:03"), Stop("Naciria", "07:09"),
            Stop("Bordj Menaiel", "07:16"), Stop("Les Issers", "07:21"),
            Stop("Si Mustapha", "07:25"), Stop("Thenia", "07:30"),
            Stop("Boumerdes", "07:40"), Stop("Reghaia", "07:50"),
            Stop("Rouiba", "07:56"), Stop("Dar El Beida", "08:02"),
            Stop("El Harrach", "08:10"), Stop("Agha", "08:20")
        )),
        // --- OUED AISSI -> THENIA

        Train("B134", listOf(
            Stop("Oued Aissi", "07:40"), Stop("Oued Aissi -Université", "07:42"), Stop("Kef Naadja", "07:47"),
            Stop("Tizi Ouzou", "07:51"), Stop("Boukhalfa", "07:55"), Stop("Draâ Ben Khedda", "07:59"),
            Stop("Tadmait", "08:09"), Stop("Naceria", "08:15"), Stop("Bordj Menaiel", "08:22"),
            Stop("Issers", "08:28"), Stop("Si Mustapha", "08:32"), Stop("Thenia", "08:37")
        )),
        Train("B140", listOf(
            Stop("Oued Aissi", "12:20"), Stop("Oued Aissi -Université", "12:22"), Stop("Kef Naadja", "12:27"),
            Stop("Tizi Ouzou", "12:31"), Stop("Boukhalfa", "12:35"), Stop("Draâ Ben Khedda", "12:39"),
            Stop("Tadmait", "12:44"), Stop("Naceria", "12:50"), Stop("Bordj Menaiel", "12:57"),
            Stop("Issers", "13:03"), Stop("Si Mustapha", "13:07"), Stop("Thenia", "13:12")
        )),
        Train("118", listOf(
            Stop("Oued Aissi", "16:20"), Stop("Oued Aissi -Université", "16:22"), Stop("Kef Naadja", "16:27"),
            Stop("Tizi Ouzou", "16:31"), Stop("Boukhalfa", "16:35"), Stop("Draâ Ben Khedda", "16:39"),
            Stop("Tadmait", "16:44"), Stop("Naceria", "16:50"), Stop("Bordj Menaiel", "16:57"),
            Stop("Issers", "17:02"), Stop("Si Mustapha", "17:06"), Stop("Thenia", "17:11")
        )),
        Train("120", listOf(
            Stop("Oued Aissi", "18:30"), Stop("Oued Aissi -Université", "18:32"), Stop("Kef Naadja", "18:37"),
            Stop("Tizi Ouzou", "18:41"), Stop("Boukhalfa", "18:45"), Stop("Draâ Ben Khedda", "18:49"),
            Stop("Tadmait", "18:59"), Stop("Naceria", "19:05"), Stop("Bordj Menaiel", "19:12"),
            Stop("Issers", "19:17"), Stop("Si Mustapha", "19:21"), Stop("Thenia", "19:26")
        )),


        // --- THENIA -> ALGER (Targeting your specific train numbers) ---

        Train("B100/505 Zéralda", listOf(Stop("Thenia", "5:55"), Stop("Tidjelabine", "6:00"), Stop("Boumerdes", "6:04"), Stop("Corso", "6:10"), Stop("Boudouaou", "6:14"), Stop("Reghaia", "6:19"), Stop("Reghaia Ind", "6:22"), Stop("Rouiba Ind", "6:23"), Stop("Rouiba", "6:25"), Stop("Dar El Beida", "6:31"), Stop("Bab Ezzouar", "6:35"), Stop("Oued Smar", "6:37"))),
        Train("22", listOf(Stop("Thenia", "06:00"), Stop("Tidjelabine", "06:04"), Stop("Boumerdes", "06:08"), Stop("Corso", "06:12"), Stop("Boudouaou", "06:15"), Stop("Reghaia", "06:21"), Stop("Reghaia Ind", "06:23"), Stop("Rouiba Ind", "06:25"), Stop("Rouiba", "06:26"), Stop("Dar El Beida", "06:32"), Stop("Bab Ezzouar", "06:35"), Stop("Oued Smar", "06:39"), Stop("El Harrach", "06:44"), Stop("Caroubier", "06:47"), Stop("Hussein Dey", "06:50"), Stop("Ateliers", "06:54"), Stop("Agha", "06:57"), Stop("Alger", "07:02"))),
        Train("28", listOf(Stop("Thenia", "06:45"), Stop("Tidjelabine", "06:49"), Stop("Boumerdes", "06:53"), Stop("Corso", "06:58"), Stop("Boudouaou", "07:01"), Stop("Reghaia", "07:07"), Stop("Reghaia Ind", "07:09"), Stop("Rouiba Ind", "07:11"), Stop("Rouiba", "07:12"), Stop("Dar El Beida", "07:18"), Stop("Bab Ezzouar", "07:21"), Stop("Oued Smar", "07:24"), Stop("El Harrach", "07:29"), Stop("Caroubier", "07:32"), Stop("Hussein Dey", "07:35"), Stop("Ateliers", "07:39"), Stop("Agha", "07:42"), Stop("Alger", "07:47"))),
        Train("12", listOf(Stop("Thenia", "07:00"), Stop("Boumerdes", "07:08"), Stop("Reghaia", "07:18"), Stop("Rouiba", "07:24"), Stop("Dar El Beida", "07:30"), Stop("El Harrach", "07:38"), Stop("Agha", "07:48"))),
        Train("34", listOf(Stop("Thenia", "08:05"), Stop("Tidjelabine", "08:09"), Stop("Boumerdes", "08:13"), Stop("Corso", "08:17"), Stop("Boudouaou", "08:20"), Stop("Reghaia", "08:26"), Stop("Reghaia Ind", "08:28"), Stop("Rouiba Ind", "08:30"), Stop("Rouiba", "08:31"), Stop("Dar El Beida", "08:37"), Stop("Bab Ezzouar", "08:40"), Stop("Oued Smar", "08:43"), Stop("El Harrach", "08:48"), Stop("Caroubier", "08:51"), Stop("Hussein Dey", "08:54"), Stop("Ateliers", "08:58"), Stop("Agha", "09:01"), Stop("Alger", "09:05"))),
        Train("B126/127 El Affroun", listOf(Stop("Thenia", "8:35"), Stop("Tidjelabine", "8:40"), Stop("Boumerdes", "8:45"), Stop("Corso", "8:50"), Stop("Boudouaou", "8:55"), Stop("Reghaia", "9:00"), Stop("Reghaia Ind", "9:03"), Stop("Rouiba Ind", "9:04"), Stop("Rouiba", "9:06"), Stop("Dar El Beida", "9:12"), Stop("Bab Ezzouar", "9:15"), Stop("Oued Smar", "9:19"))),
        Train("40", listOf(Stop("Thenia", "09:25"), Stop("Tidjelabine", "09:29"), Stop("Boumerdes", "09:33"), Stop("Corso", "09:37"), Stop("Boudouaou", "09:40"), Stop("Reghaia", "09:46"), Stop("Rouiba", "09:51"), Stop("Dar El Beida", "09:57"), Stop("Bab Ezzouar", "10:00"), Stop("Oued Smar", "10:03"), Stop("El Harrach", "10:08"), Stop("Caroubier", "10:12"), Stop("Hussein Dey", "10:15"), Stop("Ateliers", "10:19"), Stop("Agha", "10:22"), Stop("Alger", "10:27"))),
        Train("B102/507 Zéralda", listOf(Stop("Thenia", "10:20"), Stop("Tidjelabine", "10:25"), Stop("Boumerdes", "10:30"), Stop("Corso", "10:35"), Stop("Boudouaou", "10:40"), Stop("Reghaia", "10:44"), Stop("Reghaia Ind", "10:47"), Stop("Rouiba Ind", "10:48"), Stop("Rouiba", "10:50"), Stop("Dar El Beida", "10:54"), Stop("Bab Ezzouar", "10:57"), Stop("Oued Smar", "11:01"))),
        Train("44", listOf(Stop("Thenia", "10:35"), Stop("Tidjelabine", "10:39"), Stop("Boumerdes", "10:43"), Stop("Corso", "10:47"), Stop("Boudouaou", "10:50"), Stop("Reghaia", "10:56"), Stop("Rouiba", "11:01"), Stop("Dar El Beida", "11:07"), Stop("Bab Ezzouar", "11:10"), Stop("Oued Smar", "11:13"), Stop("El Harrach", "11:23"), Stop("Caroubier", "11:27"), Stop("Hussein Dey", "11:30"), Stop("Ateliers", "11:34"), Stop("Agha", "11:37"), Stop("Alger", "11:42"))),
        Train("46-H", listOf(Stop("Thenia", "11:00"), Stop("Tidjelabine", "11:04"), Stop("Boumerdes", "11:08"), Stop("Corso", "11:12"), Stop("Boudouaou", "11:15"), Stop("Reghaia", "11:21"), Stop("Rouiba", "11:26"), Stop("Dar El Beida", "11:32"), Stop("Bab Ezzouar", "11:35"), Stop("Oued Smar", "11:39"), Stop("El Harrach", "11:44"), Stop("Caroubier", "11:47"), Stop("Hussein Dey", "11:50"), Stop("Ateliers", "11:54"), Stop("Agha", "11:57"), Stop("Alger", "12:02"))),
        Train("50", listOf(Stop("Thenia", "12:00"), Stop("Tidjelabine", "12:04"), Stop("Boumerdes", "12:08"), Stop("Corso", "12:12"), Stop("Boudouaou", "12:15"), Stop("Reghaia", "12:21"), Stop("Rouiba", "12:26"), Stop("Dar El Beida", "12:32"), Stop("Bab Ezzouar", "12:35"), Stop("Oued Smar", "12:39"), Stop("El Harrach", "12:44"), Stop("Caroubier", "12:48"), Stop("Hussein Dey", "12:51"), Stop("Ateliers", "12:55"), Stop("Agha", "12:58"), Stop("Alger", "13:03"))),
        Train("56", listOf(Stop("Thenia", "13:30"), Stop("Tidjelabine", "13:34"), Stop("Boumerdes", "13:38"), Stop("Corso", "13:42"), Stop("Boudouaou", "13:45"), Stop("Reghaia", "13:51"), Stop("Rouiba", "13:56"), Stop("Dar El Beida", "14:02"), Stop("Bab Ezzouar", "14:05"), Stop("Oued Smar", "14:08"), Stop("El Harrach", "14:13"), Stop("Caroubier", "14:17"), Stop("Hussein Dey", "14:20"), Stop("Ateliers", "14:24"), Stop("Agha", "14:27"), Stop("Alger", "14:32"))),
        Train("B124/125", listOf(Stop("Reghaia", "14:40"), Stop("Reghaia Ind", "14:42"), Stop("Rouiba Ind", "14:43"), Stop("Rouiba", "14:45"), Stop("Dar El Beida", "14:50"), Stop("Bab Ezzouar", "14:53"), Stop("Oued Smar", "14:56"))),
        Train("58", listOf(Stop("Thenia", "14:20"), Stop("Tidjelabine", "14:24"), Stop("Boumerdes", "14:28"), Stop("Corso", "14:32"), Stop("Boudouaou", "14:35"), Stop("Reghaia", "14:41"), Stop("Rouiba", "14:46"), Stop("Dar El Beida", "14:52"), Stop("Bab Ezzouar", "14:55"), Stop("Oued Smar", "14:58"), Stop("El Harrach", "15:03"), Stop("Caroubier", "15:07"), Stop("Hussein Dey", "15:10"), Stop("Ateliers", "15:14"), Stop("Agha", "15:17"), Stop("Alger", "15:20"))),
        Train("62", listOf(Stop("Thenia", "15:35"), Stop("Tidjelabine", "15:39"), Stop("Boumerdes", "15:43"), Stop("Corso", "15:47"), Stop("Boudouaou", "15:50"), Stop("Reghaia", "15:56"), Stop("Reghaia Ind", "15:58"), Stop("Rouiba Ind", "16:00"), Stop("Rouiba", "16:01"), Stop("Dar El Beida", "16:07"), Stop("Bab Ezzouar", "16:10"), Stop("Oued Smar", "16:13"), Stop("El Harrach", "16:23"), Stop("Caroubier", "16:27"), Stop("Hussein Dey", "16:30"), Stop("Ateliers", "16:34"), Stop("Agha", "16:37"), Stop("Alger", "16:42"))),
        Train("B128/129", listOf(Stop("Thenia", "16:00"), Stop("Tidjelabine", "16:05"), Stop("Boumerdes", "16:10"), Stop("Corso", "16:15"), Stop("Boudouaou", "16:20"), Stop("Reghaia", "16:25"), Stop("Reghaia Ind", "16:27"), Stop("Rouiba Ind", "16:29"), Stop("Rouiba", "16:31"), Stop("Dar El Beida", "16:37"), Stop("Bab Ezzouar", "16:40"), Stop("Oued Smar", "16:44"))),
        Train("66", listOf(Stop("Thenia", "16:30"), Stop("Tidjelabine", "16:34"), Stop("Boumerdes", "16:38"), Stop("Corso", "16:42"), Stop("Boudouaou", "16:45"), Stop("Reghaia", "16:51"), Stop("Reghaia Ind", "16:53"), Stop("Rouiba Ind", "16:55"), Stop("Rouiba", "16:56"), Stop("Dar El Beida", "17:02"), Stop("Bab Ezzouar", "17:05"), Stop("Oued Smar", "17:08"), Stop("El Harrach", "17:13"), Stop("Caroubier", "17:17"), Stop("Hussein Dey", "17:20"), Stop("Ateliers", "17:24"), Stop("Agha", "17:27"), Stop("Alger", "17:32"))),
        Train("74", listOf(Stop("Thenia", "17:45"), Stop("Tidjelabine", "17:49"), Stop("Boumerdes", "17:53"), Stop("Corso", "17:57"), Stop("Boudouaou", "18:00"), Stop("Reghaia", "18:06"), Stop("Rouiba", "18:11"), Stop("Dar El Beida", "18:17"), Stop("Bab Ezzouar", "18:20"), Stop("Oued Smar", "18:23"), Stop("El Harrach", "18:28"), Stop("Caroubier", "18:32"), Stop("Hussein Dey", "18:35"), Stop("Ateliers", "18:39"), Stop("Agha", "18:42"), Stop("Alger", "18:46"))),
        Train("78", listOf(Stop("Thenia", "18:50"), Stop("Tidjelabine", "18:54"), Stop("Boumerdes", "18:58"), Stop("Corso", "19:02"), Stop("Boudouaou", "19:05"), Stop("Reghaia", "19:11"), Stop("Rouiba", "19:16"), Stop("Dar El Beida", "19:22"), Stop("Bab Ezzouar", "19:25"), Stop("Oued Smar", "19:28"), Stop("El Harrach", "19:33"), Stop("Caroubier", "19:36"), Stop("Hussein Dey", "19:39"), Stop("Ateliers", "19:43"), Stop("Agha", "19:46"), Stop("Alger", "19:51"))),

        // --- AGHA -> ZÉRALDA

        Train("1501", listOf(
            Stop("Agha", "05:10"), Stop("Ateliers", "05:12"), Stop("Hussein Dey", "05:16"),
            Stop("Caroubier", "05:19"), Stop("El Harrach", "05:22"), Stop("Gué de Cne", "05:26"),
            Stop("Ain Naadja", "05:29"), Stop("Baba Ali", "05:33"), Stop("Birtouta", "05:38"),
            Stop("Tessala ElMerdja", "05:46"), Stop("Sidi AbdAllah", "05:51"),
            Stop("Sidi AbdAllah-Univ.", "05:54"), Stop("Zéralda", "05:58")
        )),
        Train("1505", listOf(
            Stop("Agha", "07:30"), Stop("Ateliers", "07:32"), Stop("Hussein Dey", "07:36"),
            Stop("Caroubier", "07:39"), Stop("El Harrach", "07:42"), Stop("Gué de Cne", "07:46"),
            Stop("Ain Naadja", "07:49"), Stop("Baba Ali", "07:53"), Stop("Birtouta", "07:58"),
            Stop("Tessala ElMerdja", "08:06"), Stop("Sidi AbdAllah", "08:11"),
            Stop("Sidi AbdAllah-Univ.", "08:14"), Stop("Zéralda", "08:18")
        )),
        Train("1509", listOf(
            Stop("Agha", "08:40"), Stop("Ateliers", "08:42"), Stop("Hussein Dey", "08:46"),
            Stop("Caroubier", "08:49"), Stop("El Harrach", "08:52"), Stop("Gué de Cne", "08:56"),
            Stop("Ain Naadja", "08:59"), Stop("Baba Ali", "09:03"), Stop("Birtouta", "09:08"),
            Stop("Tessala ElMerdja", "09:16"), Stop("Sidi AbdAllah", "09:21"),
            Stop("Sidi AbdAllah-Univ.", "09:24"), Stop("Zéralda", "09:28")
        )),
        Train("1513", listOf(
            Stop("Agha", "10:15"), Stop("Ateliers", "10:17"), Stop("Hussein Dey", "10:21"),
            Stop("Caroubier", "10:24"), Stop("El Harrach", "10:27"), Stop("Gué de Cne", "10:31"),
            Stop("Ain Naadja", "10:34"), Stop("Baba Ali", "10:38"), Stop("Birtouta", "10:43"),
            Stop("Tessala ElMerdja", "10:51"), Stop("Sidi AbdAllah", "10:56"),
            Stop("Sidi AbdAllah-Univ.", "10:59"), Stop("Zéralda", "11:03")
        )),
        Train("1525", listOf(
            Stop("Agha", "16:25"), Stop("Ateliers", "16:27"), Stop("Hussein Dey", "16:31"),
            Stop("Caroubier", "16:34"), Stop("El Harrach", "16:37"), Stop("Gué de Cne", "16:41"),
            Stop("Ain Naadja", "16:44"), Stop("Baba Ali", "16:48"), Stop("Birtouta", "16:53"),
            Stop("Tessala ElMerdja", "17:01"), Stop("Sidi AbdAllah", "17:06"),
            Stop("Sidi AbdAllah-Univ.", "17:09"), Stop("Zéralda", "17:13")
        )),
        Train("1529", listOf(
            Stop("Agha", "18:35"), Stop("Ateliers", "18:37"), Stop("Hussein Dey", "18:41"),
            Stop("Caroubier", "18:44"), Stop("El Harrach", "18:47"), Stop("Gué de Cne", "18:51"),
            Stop("Ain Naadja", "18:54"), Stop("Baba Ali", "18:58"), Stop("Birtouta", "19:03"),
            Stop("Tessala ElMerdja", "19:11"), Stop("Sidi AbdAllah", "19:16"),
            Stop("Sidi AbdAllah-Univ.", "19:19"), Stop("Zéralda", "19:23")
        )),

        // --- ZÉRALDA -> AGHA

        Train("1500", listOf(
            Stop("Zéralda", "06:15"), Stop("Sidi AbdAllah-Univ.", "06:18"), Stop("Sidi AbdAllah", "06:21"),
            Stop("Tessala ElMerdja", "06:26"), Stop("Birtouta", "06:32"), Stop("Baba Ali", "06:37"),
            Stop("Ain Naadja", "06:41"), Stop("Gué Cne", "06:44"), Stop("El Harrach", "06:48"),
            Stop("Caroubier", "06:51"), Stop("Hussein Dey", "06:54"), Stop("Ateliers", "06:57"), Stop("Agha", "07:01")
        )),
        Train("1504", listOf(
            Stop("Zéralda", "08:30"), Stop("Sidi AbdAllah-Univ.", "08:33"), Stop("Sidi AbdAllah", "08:36"),
            Stop("Tessala ElMerdja", "08:42"), Stop("Birtouta", "08:49"), Stop("Baba Ali", "08:54"),
            Stop("Ain Naadja", "08:58"), Stop("Gué Cne", "09:01"), Stop("El Harrach", "09:05"),
            Stop("Caroubier", "09:08"), Stop("Hussein Dey", "09:11"), Stop("Ateliers", "09:15"), Stop("Agha", "09:18")
        )),
        Train("1512", listOf(
            Stop("Zéralda", "11:30"), Stop("Sidi AbdAllah-Univ.", "11:33"), Stop("Sidi AbdAllah", "11:36"),
            Stop("Tessala ElMerdja", "11:42"), Stop("Birtouta", "11:49"), Stop("Baba Ali", "11:54"),
            Stop("Ain Naadja", "11:58"), Stop("Gué Cne", "12:01"), Stop("El Harrach", "12:05"),
            Stop("Caroubier", "12:08"), Stop("Hussein Dey", "12:11"), Stop("Ateliers", "12:15"), Stop("Agha", "12:18")
        )),
        Train("1522", listOf(
            Stop("Zéralda", "16:30"), Stop("Sidi AbdAllah-Univ.", "16:33"), Stop("Sidi AbdAllah", "16:36"),
            Stop("Tessala ElMerdja", "16:42"), Stop("Birtouta", "16:49"), Stop("Baba Ali", "16:54"),
            Stop("Ain Naadja", "16:58"), Stop("Gué Cne", "17:01"), Stop("El Harrach", "17:05"),
            Stop("Caroubier", "17:08"), Stop("Hussein Dey", "17:11"), Stop("Ateliers", "17:15"), Stop("Agha", "17:18")
        )),
        Train("1526", listOf(
            Stop("Zéralda", "18:35"), Stop("Sidi AbdAllah-Univ.", "18:38"), Stop("Sidi AbdAllah", "18:41"),
            Stop("Tessala ElMerdja", "18:47"), Stop("Birtouta", "18:54"), Stop("Baba Ali", "19:00"),
            Stop("Ain Naadja", "19:04"), Stop("Gué Cne", "19:07"), Stop("El Harrach", "19:12"),
            Stop("Caroubier", "19:17"), Stop("Hussein Dey", "19:20"), Stop("Ateliers", "19:24"), Stop("Agha", "19:27")
        )),

        // --- ALGER -> EL AFFROUN

        Train("1025", listOf(Stop("Alger", "05:45"), Stop("Agha", "05:48"), Stop("Ateliers", "05:51"), Stop("Hussein Dey", "05:55"), Stop("Caroubier", "05:58"), Stop("El Harrach", "06:01"), Stop("Gué de Cne", "06:05"), Stop("Ain Naadja", "06:08"), Stop("Baba Ali", "06:12"), Stop("Birtouta", "06:17"), Stop("Boufarik", "06:25"), Stop("Beni Mered", "06:31"), Stop("Blida", "06:37"), Stop("Chiffa", "06:43"), Stop("Mouzaia", "06:47"), Stop("El Affroun", "06:52"))),
        Train("1029", listOf(Stop("Alger", "07:10"), Stop("Agha", "07:13"), Stop("Ateliers", "07:17"), Stop("Hussein Dey", "07:21"), Stop("Caroubier", "07:24"), Stop("El Harrach", "07:27"), Stop("Gué de Cne", "07:32"), Stop("Ain Naadja", "07:35"), Stop("Baba Ali", "07:39"), Stop("Birtouta", "07:44"), Stop("Boufarik", "07:52"), Stop("Beni Mered", "07:58"), Stop("Blida", "08:04"), Stop("Chiffa", "08:11"), Stop("Mouzaia", "08:15"), Stop("El Affroun", "08:20"))),
        Train("1031", listOf(Stop("Hussein Dey", "08:10"), Stop("Caroubier", "08:11"), Stop("El Harrach", "08:14"), Stop("Gué de Cne", "08:19"), Stop("Ain Naadja", "08:22"), Stop("Baba Ali", "08:26"), Stop("Birtouta", "08:31"), Stop("Boufarik", "08:39"), Stop("Beni Mered", "08:45"), Stop("Blida", "08:51"), Stop("Chiffa", "08:57"), Stop("Mouzaia", "09:01"), Stop("El Affroun", "09:06"))),
        Train("1033", listOf(Stop("Hussein Dey", "08:50"), Stop("Caroubier", "08:51"), Stop("El Harrach", "08:54"), Stop("Gué de Cne", "08:59"), Stop("Ain Naadja", "09:02"), Stop("Baba Ali", "09:06"), Stop("Birtouta", "09:11"), Stop("Boufarik", "09:19"), Stop("Beni Mered", "09:25"), Stop("Blida", "09:31"), Stop("Chiffa", "09:37"), Stop("Mouzaia", "09:41"), Stop("El Affroun", "09:46"))),
        Train("1035-H", listOf(Stop("Hussein Dey", "09:10"), Stop("Caroubier", "09:14"), Stop("El Harrach", "09:17"), Stop("Gué de Cne", "09:22"), Stop("Ain Naadja", "09:25"), Stop("Baba Ali", "09:29"), Stop("Birtouta", "09:34"), Stop("Boufarik", "09:42"), Stop("Beni Mered", "09:48"), Stop("Blida", "09:54"), Stop("Chiffa", "10:01"), Stop("Mouzaia", "10:05"), Stop("El Affroun", "10:10"))),
        Train("B126", listOf(Stop("Gué de Cne", "09:29"), Stop("Ain Naadja", "09:32"), Stop("Baba Ali", "09:36"), Stop("Birtouta", "09:43"), Stop("Boufarik", "09:53"), Stop("Beni Mered", "10:01"), Stop("Blida", "10:09"), Stop("Chiffa", "10:16"), Stop("Mouzaia", "10:20"), Stop("El Affroun", "10:25"))),

        Train("1037", listOf(Stop("Hussein Dey", "10:10"), Stop("Caroubier", "10:11"), Stop("El Harrach", "10:14"), Stop("Gué de Cne", "10:19"), Stop("Ain Naadja", "10:22"), Stop("Baba Ali", "10:26"), Stop("Birtouta", "10:31"), Stop("Boufarik", "10:39"), Stop("Beni Mered", "10:45"), Stop("Blida", "10:51"), Stop("Chiffa", "10:58"), Stop("Mouzaia", "11:02"), Stop("El Affroun", "11:07"))),
        Train("1041-H", listOf(Stop("Hussein Dey", "11:10"), Stop("Caroubier", "11:14"), Stop("El Harrach", "11:17"), Stop("Gué de Cne", "11:22"), Stop("Ain Naadja", "11:25"), Stop("Baba Ali", "11:29"), Stop("Birtouta", "11:34"), Stop("Boufarik", "11:42"), Stop("Beni Mered", "11:48"), Stop("Blida", "11:54"), Stop("Chiffa", "12:01"), Stop("Mouzaia", "12:05"), Stop("El Affroun", "12:10"))),
        Train("1041", listOf(Stop("Hussein Dey", "11:30"), Stop("Caroubier", "11:34"), Stop("El Harrach", "11:37"), Stop("Gué de Cne", "11:42"), Stop("Ain Naadja", "11:45"), Stop("Baba Ali", "11:49"), Stop("Birtouta", "11:54"), Stop("Boufarik", "12:02"), Stop("Beni Mered", "12:08"), Stop("Blida", "12:14"), Stop("Chiffa", "12:21"), Stop("Mouzaia", "12:25"), Stop("El Affroun", "12:30"))),
        Train("1045", listOf(Stop("Hussein Dey", "12:15"), Stop("Caroubier", "12:19"), Stop("El Harrach", "12:22"), Stop("Gué de Cne", "12:27"), Stop("Ain Naadja", "12:40"), Stop("Baba Ali", "12:44"), Stop("Birtouta", "12:49"), Stop("Boufarik", "12:57"), Stop("Beni Mered", "13:03"), Stop("Blida", "13:09"), Stop("Chiffa", "13:16"), Stop("Mouzaia", "13:20"), Stop("El Affroun", "13:25"))),
        Train("1049-H", listOf(Stop("Hussein Dey", "13:30"), Stop("Caroubier", "13:34"), Stop("El Harrach", "13:37"), Stop("Gué de Cne", "13:42"), Stop("Ain Naadja", "13:45"), Stop("Baba Ali", "13:49"), Stop("Birtouta", "13:54"), Stop("Boufarik", "14:02"), Stop("Beni Mered", "14:08"), Stop("Blida", "14:14"), Stop("Chiffa", "14:21"), Stop("Mouzaia", "14:25"), Stop("El Affroun", "14:30"))),
        Train("1051", listOf(Stop("Hussein Dey", "14:10"), Stop("Caroubier", "14:14"), Stop("El Harrach", "14:17"), Stop("Gué de Cne", "14:22"), Stop("Ain Naadja", "14:25"), Stop("Baba Ali", "14:29"), Stop("Birtouta", "14:34"), Stop("Boufarik", "14:42"), Stop("Beni Mered", "14:48"), Stop("Blida", "14:54"), Stop("Chiffa", "15:01"), Stop("Mouzaia", "15:05"), Stop("El Affroun", "15:10"))),

        Train("1053-H", listOf(Stop("Hussein Dey", "15:21"), Stop("Caroubier", "15:24"), Stop("El Harrach", "15:27"), Stop("Gué de Cne", "15:32"), Stop("Ain Naadja", "15:35"), Stop("Baba Ali", "15:39"), Stop("Birtouta", "15:44"), Stop("Boufarik", "15:52"), Stop("Beni Mered", "15:58"), Stop("Blida", "16:04"), Stop("Chiffa", "16:11"), Stop("Mouzaia", "16:15"), Stop("El Affroun", "16:20"))),
        Train("1055", listOf(Stop("Hussein Dey", "15:55"), Stop("Caroubier", "15:59"), Stop("El Harrach", "16:02"), Stop("Gué de Cne", "16:07"), Stop("Ain Naadja", "16:10"), Stop("Baba Ali", "16:14"), Stop("Birtouta", "16:19"), Stop("Boufarik", "16:27"), Stop("Beni Mered", "16:33"), Stop("Blida", "16:39"), Stop("Chiffa", "16:46"), Stop("Mouzaia", "16:50"), Stop("El Affroun", "16:55"))),
        Train("1057", listOf(Stop("Alger", "16:25"), Stop("Agha", "16:28"), Stop("Ateliers", "16:32"), Stop("Hussein Dey", "16:36"), Stop("Caroubier", "16:39"), Stop("El Harrach", "16:42"), Stop("Gué de Cne", "16:47"), Stop("Ain Naadja", "16:50"), Stop("Baba Ali", "16:54"), Stop("Birtouta", "16:59"), Stop("Boufarik", "17:07"), Stop("Beni Mered", "17:13"), Stop("Blida", "17:19"), Stop("Chiffa", "17:25"), Stop("Mouzaia", "17:29"), Stop("El Affroun", "17:34"))),
        Train("B128", listOf(Stop("Gué de Cne", "16:54"), Stop("Ain Naadja", "16:57"), Stop("Baba Ali", "17:01"), Stop("Birtouta", "17:08"), Stop("Boufarik", "17:18"), Stop("Beni Mered", "17:26"), Stop("Blida", "17:34"), Stop("Chiffa", "17:41"), Stop("Mouzaia", "17:45"), Stop("El Affroun", "17:50"))),
        Train("1061", listOf(Stop("Hussein Dey", "17:30"), Stop("Caroubier", "17:34"), Stop("El Harrach", "17:37"), Stop("Gué de Cne", "17:42"), Stop("Ain Naadja", "17:45"), Stop("Baba Ali", "17:49"), Stop("Birtouta", "17:54"), Stop("Boufarik", "18:02"), Stop("Beni Mered", "18:08"), Stop("Blida", "18:14"), Stop("Chiffa", "18:21"), Stop("Mouzaia", "18:25"), Stop("El Affroun", "18:30"))),
        Train("1065", listOf(Stop("Hussein Dey", "18:30"), Stop("Caroubier", "18:34"), Stop("El Harrach", "18:37"), Stop("Gué de Cne", "18:42"), Stop("Ain Naadja", "18:45"), Stop("Baba Ali", "18:49"), Stop("Birtouta", "18:54"), Stop("Boufarik", "19:02"), Stop("Beni Mered", "19:08"), Stop("Blida", "19:14"), Stop("Chiffa", "19:21"), Stop("Mouzaia", "19:25"), Stop("El Affroun", "19:30"))),

        // --- EL AFFROUN -> ALGER

        Train("1022", listOf(Stop("El Affroun", "05:40"), Stop("Mouzaia", "05:44"), Stop("Chiffa", "05:48"), Stop("Blida", "05:54"), Stop("Beni Mered", "06:01"), Stop("Boufarik", "06:07"), Stop("Birtouta", "06:15"), Stop("Baba Ali", "06:20"), Stop("Ain Naadja", "06:24"), Stop("Gué de Cne", "06:27"), Stop("El Harrach", "06:31"), Stop("Caroubier", "06:34"), Stop("Hussein Dey", "06:37"), Stop("Ateliers", "06:41"), Stop("Agha", "06:44"), Stop("Alger", "06:48"))),
        Train("B152/153", listOf(Stop("El Affroun", "06:00"), Stop("Mouzaia", "06:04"), Stop("Chiffa", "06:08"), Stop("Blida", "06:14"), Stop("Beni Mered", "06:19"), Stop("Boufarik", "06:27"), Stop("Birtouta", "06:37"), Stop("Baba Ali", "06:44"), Stop("Ain Naadja", "06:47"), Stop("Gué de Cne", "06:50"))), // Note: Continues to Thénia
        Train("1024", listOf(Stop("El Affroun", "06:30"), Stop("Mouzaia", "06:34"), Stop("Chiffa", "06:38"), Stop("Blida", "06:44"), Stop("Beni Mered", "06:50"), Stop("Boufarik", "06:56"), Stop("Birtouta", "07:04"), Stop("Baba Ali", "07:09"), Stop("Ain Naadja", "07:13"), Stop("Gué de Cne", "07:16"), Stop("El Harrach", "07:20"), Stop("Caroubier", "07:25"), Stop("Hussein Dey", "07:28"))),
        Train("1030", listOf(Stop("El Affroun", "07:25"), Stop("Mouzaia", "07:29"), Stop("Chiffa", "07:33"), Stop("Blida", "07:39"), Stop("Beni Mered", "07:46"), Stop("Boufarik", "07:52"), Stop("Birtouta", "08:00"), Stop("Baba Ali", "08:05"), Stop("Ain Naadja", "08:09"), Stop("Gué de Cne", "08:12"), Stop("El Harrach", "08:16"), Stop("Caroubier", "08:20"), Stop("Hussein Dey", "08:23"))),
        Train("1034", listOf(Stop("El Affroun", "08:45"), Stop("Mouzaia", "08:49"), Stop("Chiffa", "08:53"), Stop("Blida", "08:59"), Stop("Beni Mered", "09:06"), Stop("Boufarik", "09:12"), Stop("Birtouta", "09:20"), Stop("Baba Ali", "09:25"), Stop("Ain Naadja", "09:29"), Stop("Gué de Cne", "09:32"), Stop("El Harrach", "09:36"), Stop("Caroubier", "09:40"), Stop("Hussein Dey", "09:43"))),
        Train("1036-H", listOf(Stop("El Affroun", "09:30"), Stop("Mouzaia", "09:34"), Stop("Chiffa", "09:38"), Stop("Blida", "09:44"), Stop("Beni Mered", "09:50"), Stop("Boufarik", "09:56"), Stop("Birtouta", "10:04"), Stop("Baba Ali", "10:09"), Stop("Ain Naadja", "10:13"), Stop("Gué de Cne", "10:16"), Stop("El Harrach", "10:20"), Stop("Caroubier", "10:25"), Stop("Hussein Dey", "10:28"))),

        Train("1038", listOf(Stop("El Affroun", "10:00"), Stop("Mouzaia", "10:04"), Stop("Chiffa", "10:08"), Stop("Blida", "10:14"), Stop("Beni Mered", "10:21"), Stop("Boufarik", "10:27"), Stop("Birtouta", "10:35"), Stop("Baba Ali", "10:40"), Stop("Ain Naadja", "10:44"), Stop("Gué de Cne", "10:47"), Stop("El Harrach", "10:51"), Stop("Caroubier", "10:55"), Stop("Hussein Dey", "10:58"))),
        Train("1040", listOf(Stop("El Affroun", "10:45"), Stop("Mouzaia", "10:49"), Stop("Chiffa", "10:53"), Stop("Blida", "10:59"), Stop("Beni Mered", "11:06"), Stop("Boufarik", "11:12"), Stop("Birtouta", "11:20"), Stop("Baba Ali", "11:25"), Stop("Ain Naadja", "11:29"), Stop("Gué de Cne", "11:32"), Stop("El Harrach", "11:36"), Stop("Caroubier", "11:40"), Stop("Hussein Dey", "11:43"))),
        Train("1042-H", listOf(Stop("El Affroun", "11:45"), Stop("Mouzaia", "11:49"), Stop("Chiffa", "11:53"), Stop("Blida", "11:59"), Stop("Beni Mered", "12:06"), Stop("Boufarik", "12:12"), Stop("Birtouta", "12:20"), Stop("Baba Ali", "12:25"), Stop("Ain Naadja", "12:29"), Stop("Gué de Cne", "12:32"), Stop("El Harrach", "12:36"), Stop("Caroubier", "12:40"), Stop("Hussein Dey", "12:43"))),
        Train("1044", listOf(Stop("El Affroun", "12:25"), Stop("Mouzaia", "12:29"), Stop("Chiffa", "12:33"), Stop("Blida", "12:39"), Stop("Beni Mered", "12:45"), Stop("Boufarik", "12:51"), Stop("Birtouta", "12:59"), Stop("Baba Ali", "13:04"), Stop("Ain Naadja", "13:08"), Stop("Gué de Cne", "13:11"), Stop("El Harrach", "13:15"), Stop("Caroubier", "13:18"), Stop("Hussein Dey", "13:21"))),
        Train("B154/155", listOf(Stop("El Affroun", "13:05"), Stop("Mouzaia", "13:09"), Stop("Chiffa", "13:13"), Stop("Blida", "13:19"), Stop("Beni Mered", "13:26"), Stop("Boufarik", "13:34"), Stop("Birtouta", "13:44"), Stop("Baba Ali", "13:48"), Stop("Ain Naadja", "13:51"), Stop("Gué de Cne", "13:54"))), // Note: Continues to Thénia
        Train("1048", listOf(Stop("El Affroun", "13:30"), Stop("Mouzaia", "13:34"), Stop("Chiffa", "13:38"), Stop("Blida", "13:44"), Stop("Beni Mered", "13:50"), Stop("Boufarik", "13:56"), Stop("Birtouta", "14:04"), Stop("Baba Ali", "14:09"), Stop("Ain Naadja", "14:13"), Stop("Gué de Cne", "14:16"), Stop("El Harrach", "14:20"), Stop("Caroubier", "14:23"), Stop("Hussein Dey", "14:26"), Stop("Ateliers", "14:30"), Stop("Agha", "14:33"), Stop("Alger", "14:37"))),

        Train("1050-H", listOf(Stop("El Affroun", "14:00"), Stop("Mouzaia", "14:04"), Stop("Chiffa", "14:08"), Stop("Blida", "14:14"), Stop("Beni Mered", "14:21"), Stop("Boufarik", "14:27"), Stop("Birtouta", "14:35"), Stop("Baba Ali", "14:40"), Stop("Ain Naadja", "14:44"), Stop("Gué de Cne", "14:47"), Stop("El Harrach", "14:51"), Stop("Caroubier", "14:55"), Stop("Hussein Dey", "14:58"))),
        Train("1052", listOf(Stop("El Affroun", "14:20"), Stop("Mouzaia", "14:24"), Stop("Chiffa", "14:28"), Stop("Blida", "14:34"), Stop("Beni Mered", "14:41"), Stop("Boufarik", "14:47"), Stop("Birtouta", "14:55"), Stop("Baba Ali", "15:00"), Stop("Ain Naadja", "15:04"), Stop("Gué de Cne", "15:07"), Stop("El Harrach", "15:11"), Stop("Caroubier", "15:15"), Stop("Hussein Dey", "15:18"))),
        Train("1054-H", listOf(Stop("El Affroun", "15:00"), Stop("Mouzaia", "15:04"), Stop("Chiffa", "15:08"), Stop("Blida", "15:14"), Stop("Beni Mered", "15:21"), Stop("Boufarik", "15:27"), Stop("Birtouta", "15:35"), Stop("Baba Ali", "15:40"), Stop("Ain Naadja", "15:44"), Stop("Gué de Cne", "15:47"), Stop("El Harrach", "15:51"), Stop("Caroubier", "15:55"), Stop("Hussein Dey", "15:58"), Stop("Ateliers", "16:02"), Stop("Agha", "16:05"), Stop("Alger", "16:10"))),
        Train("1056", listOf(Stop("El Affroun", "16:00"), Stop("Mouzaia", "16:04"), Stop("Chiffa", "16:08"), Stop("Blida", "16:14"), Stop("Beni Mered", "16:21"), Stop("Boufarik", "16:27"), Stop("Birtouta", "16:35"), Stop("Baba Ali", "16:40"), Stop("Ain Naadja", "16:44"), Stop("Gué de Cne", "16:47"), Stop("El Harrach", "16:51"), Stop("Caroubier", "16:55"), Stop("Hussein Dey", "16:58"))),
        Train("1060", listOf(Stop("El Affroun", "17:20"), Stop("Mouzaia", "17:24"), Stop("Chiffa", "17:28"), Stop("Blida", "17:34"), Stop("Beni Mered", "17:41"), Stop("Boufarik", "17:47"), Stop("Birtouta", "17:55"), Stop("Baba Ali", "18:00"), Stop("Ain Naadja", "18:04"), Stop("Gué de Cne", "18:07"), Stop("El Harrach", "18:11"), Stop("Caroubier", "18:16"), Stop("Hussein Dey", "18:19"))),
        Train("1060-H", listOf(Stop("El Affroun", "17:10"), Stop("Mouzaia", "17:14"), Stop("Chiffa", "17:18"), Stop("Blida", "17:24"), Stop("Beni Mered", "17:31"), Stop("Boufarik", "17:37"), Stop("Birtouta", "17:45"), Stop("Baba Ali", "17:50"), Stop("Ain Naadja", "17:54"), Stop("Gué de Cne", "17:57"), Stop("El Harrach", "18:01"), Stop("Caroubier", "18:06"), Stop("Hussein Dey", "18:09"))),
        Train("1066", listOf(Stop("El Affroun", "18:30"), Stop("Mouzaia", "18:34"), Stop("Chiffa", "18:38"), Stop("Blida", "18:44"), Stop("Beni Mered", "18:50"), Stop("Boufarik", "18:56"), Stop("Birtouta", "19:04"), Stop("Baba Ali", "19:09"), Stop("Ain Naadja", "19:13"), Stop("Gué de Cne", "19:16"), Stop("El Harrach", "19:20"), Stop("Caroubier", "19:23"), Stop("Hussein Dey", "19:26"), Stop("Ateliers", "19:30"), Stop("Agha", "19:33"), Stop("Alger", "19:38"))),

    )

    // Helper to get the full list for the Reset button
    fun getFullSchedule(): List<Train> = allTrains

    val stations = listOf(
        "Agha", "Ain Naadja", "Alger", "Ateliers", "Baba Ali", "Bab Ezzouar",
        "Birtouta", "Bordj Menaiel", "Boudouaou", "Boukhalfa", "Boumerdes",
        "Boufarik", "Beni Mered", "Blida", "Caroubier", "Chiffa", "Corso",
        "Dar El Beida", "Draâ Ben Khedda", "El Affroun", "El Harrach",
        "Gué de Cne", "Hussein Dey", "Issers", "Kef Naadja", "Mouzaia",
        "Naceria", "Oued Aissi", "Oued Aissi -Univ.", "Oued Smar", "Reghaia",
        "Reghaia ZI", "Rouiba", "Rouiba ZI", "Sidi AbdAllah", "Sidi AbdAllah-Univ.",
        "Si Mustapha", "Tadmait", "Tessala ElMerdja", "Thenia", "Tidjelabine",
        "Tizi Ouzou", "Zéralda"
    ).sorted()
}

class MainActivity : AppCompatActivity() {

    private var selectedTime = ""

    private var trainAdapter: TrainAdapter? = null

    private fun saveFavorite(from: String, to: String) {
        if (from.isEmpty() || to.isEmpty()) return

        val prefs = getSharedPreferences("DZTrainPrefs", MODE_PRIVATE)
        val gson = Gson()
        val favorites = getFavorites().toMutableList()

        // Create new favorite including the global selectedTime
        val newFav = FavoriteRoute(from, to, selectedTime)

        if (!favorites.any { it.from == from && it.to == to }) {
            favorites.add(newFav)
            val json = gson.toJson(favorites)
            prefs.edit().putString("fav_routes", json).apply()
            updateFavoritesUI()
        }
    }

    private fun getFavorites(): List<FavoriteRoute> {
        val prefs = getSharedPreferences("DZTrainPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("fav_routes", null) ?: return emptyList()
        val type = object : TypeToken<List<FavoriteRoute>>() {}.type
        return Gson().fromJson(json, type)
    }
    private fun showAboutDialog() {
        val builder = android.app.AlertDialog.Builder(this)

        // Colors updated to match your timetable and popup logic
        val message = """
    <b><i><font color='#1976D2'>    version v2.0</font></i></b><br><br>
    This app provides information about SNTF Suburban Schedule.<br><br>
    ______________________________________<br><br>
    Color code for available lines:<br>
    <font color='#1976D2'>▪ Alger - Thénia </font><br>
    <font color='#CB7DA2'>▪ Alger - El Affroun </font><br>
    <font color='#2E7D32'>▪ Alger - Zéralda </font><br>
    <font color='#FB8C00'>▪ Alger - Bouira </font><br>
    <font color='#7B1FA2'>▪ Alger - Tizi Ouzou / Oued Aissi </font><br>
    ______________________________________<br><br>
    <i><font color='#C2185B'>Created by MAHDAOUI Y.</font></i>
""".trimIndent()

        builder.setTitle("DZTrain")
        builder.setMessage(android.text.Html.fromHtml(message, android.text.Html.FROM_HTML_MODE_LEGACY))

        builder.setPositiveButton("X") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()

        // Sets the 'Close' button to the primary Blue color used for El Affroun/Thénia
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.parseColor("#1976D2"))
        val titleId = resources.getIdentifier("alertTitle", "id", "android")
        if (titleId > 0) {
            dialog.findViewById<android.widget.TextView>(titleId)?.setTextColor(android.graphics.Color.parseColor("#1976D2"))
        }
    }

    private lateinit var startInput: AutoCompleteTextView
    private lateinit var targetInput: AutoCompleteTextView
    private lateinit var btnFav: Button
    private lateinit var btnSearch: Button
    private lateinit var btnReset: Button
    private lateinit var btnSwap: Button
    private lateinit var ivNoResults: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvHeader: TextView
    private lateinit var btnTime: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateFavoritesUI()

        // Get current time from the system
        val calendar = java.util.Calendar.getInstance()
        val currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(java.util.Calendar.MINUTE)

        // Format and set the initial selectedTime
        selectedTime = String.format("%02d:%02d", currentHour, currentMinute)

        // Update the button text to show the current time immediately
        btnTime = findViewById<Button>(R.id.btnTime)
        btnTime.text = "Departure Time $selectedTime"

        startInput = findViewById<AutoCompleteTextView>(R.id.startStation)
        targetInput = findViewById<AutoCompleteTextView>(R.id.targetStation)
        btnSearch = findViewById<Button>(R.id.btnSearch)
        btnReset = findViewById<Button>(R.id.btnReset)
        btnSwap = findViewById<Button>(R.id.btnSwap)
        ivNoResults = findViewById<ImageView>(R.id.ivNoResults)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        tvHeader = findViewById<TextView>(R.id.tvHeader)
        btnFav = findViewById<Button>(R.id.btnFav)

        btnFav.setOnClickListener {
            val from = startInput.text.toString()
            val to = targetInput.text.toString()
            saveFavorite(from, to) // Saves the route to your list
        }

        tvHeader.setOnClickListener {
            showAboutDialog()
        }

        // Initialize RecyclerView LayoutManager once
        recyclerView.layoutManager = LinearLayoutManager(this)

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
            // Get fresh time in case the user has been on the app for a while
            val now = java.util.Calendar.getInstance()
            val h = now.get(java.util.Calendar.HOUR_OF_DAY)
            val m = now.get(java.util.Calendar.MINUTE)

            TimePickerDialog(this, { _, hour, minute ->
                selectedTime = String.format("%02d:%02d", hour, minute)
                btnTime.text = "Departure Time $selectedTime"
            }, h, m, true).show() // Opens at current hour/minute
        }

        // Search Logic
        btnSearch.setOnClickListener { performSearch()
        }

        btnReset.setOnClickListener {
            targetInput.text.clear()
            startInput.text.clear()

            // Reset clock to "Now"
            val now = java.util.Calendar.getInstance()
            selectedTime = String.format("%02d:%02d",
                now.get(java.util.Calendar.HOUR_OF_DAY),
                now.get(java.util.Calendar.MINUTE)
            )
            btnTime.text = "Departure Time $selectedTime"

            // FIX: Pass ALL three required parameters to the TrainAdapter
            trainAdapter = TrainAdapter(
                list = emptyList(),
                onItemClick = { train -> showStopsPopup(this, train) }, // Parameter 2
                onLongClick = { train -> shareTrainDetails(train) }     // Parameter 3
            )
            recyclerView.adapter = trainAdapter

            // Animation logic
            if (ivNoResults.visibility == View.VISIBLE) {
                ivNoResults.animate().alpha(0f).setDuration(500).withEndAction {
                    ivNoResults.visibility = View.GONE
                }.start()
            }
            recyclerView.requestFocus()
        }

    }

    private fun shareTrainDetails(result: SearchResult) {
        val message = "Trip Info: ${result.from} -> ${result.to}\n" +
                "Train: ${result.id}\nDeparture: ${result.depTime}\nArrival: ${result.arrTime}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(Intent.createChooser(intent, "Share Schedule"))
    }

    private fun performSearch() {
        val start = startInput.text.toString().trim()
        val target = targetInput.text.toString().trim()

        // 1. Validation check
        if (start.isEmpty() || target.isEmpty()) {
            Toast.makeText(this, "Please select both stops!", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Filter logic using your TimeRepository and selectedTime
        val filtered = TrainRepository.allTrains.mapNotNull { train ->
            val sIdx = train.stops.indexOfFirst { it.name.equals(start, true) }
            val tIdx = train.stops.indexOfFirst { it.name.equals(target, true) }

            // Ensure both stations exist and the train is moving in the right direction
            if (sIdx != -1 && tIdx != -1 && tIdx > sIdx) {
                val dep = train.stops[sIdx].time
                val arr = train.stops[tIdx].time

                // Filter out '-' markers and apply the time filter
                if (dep != "-" && arr != "-" && dep >= selectedTime) {
                    SearchResult(
                        id = train.id,
                        depTime = dep,
                        arrTime = arr,
                        from = start,
                        to = target,
                        stops = train.stops
                    )
                } else null
            } else null
        }.sortedBy { it.depTime }

        // 3. UI Update with Fade Animation
        if (filtered.isEmpty()) {
            recyclerView.visibility = View.GONE
            ivNoResults.visibility = View.VISIBLE

            // Reset alpha and animate the fade-in for the empty state
            ivNoResults.alpha = 0f
            ivNoResults.animate().alpha(1f).setDuration(500).start()
        } else {
            ivNoResults.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            // Setup LayoutManager and Adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = TrainAdapter(
                list = filtered,
                onItemClick = { train ->
                    showStopsPopup(this, train) // This handles the normal tap
                },
                onLongClick = { train ->
                    shareTrainDetails(train) // This handles the long press
                }
            )
        }
        recyclerView.requestFocus()
    }

    private fun updateFavoritesUI() {
        val favList = getFavorites()
        val favRecycler = findViewById<RecyclerView>(R.id.favoritesRecyclerView)


        val adapter = FavoritesAdapter(favList,
            onClick = { selectedFav ->
                // 1. Restore Stations
                startInput.setText(selectedFav.from)
                targetInput.setText(selectedFav.to)

                // 2. Restore Time
                selectedTime = selectedFav.time
                btnTime.text = "Departure Time $selectedTime" // Update your time button text

                // 3. Hide the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(startInput.windowToken, 0)

                // 4. Trigger the search
                performSearch()
            },
            onLongClick = { routeToDelete ->
                deleteFavorite(routeToDelete)
            }
        )
        favRecycler.adapter = adapter
    }

    private fun deleteFavorite(route: FavoriteRoute) {
        val prefs = getSharedPreferences("DZTrainPrefs", MODE_PRIVATE)
        val gson = Gson()

        // 1. Get the current list of favorites
        val favorites = getFavorites().toMutableList()

        // 2. Remove the specific route that matches the 'from', 'to', and 'time'
        favorites.removeAll {
            it.from == route.from &&
                    it.to == route.to &&
                    it.time == route.time
        }

        // 3. Save the updated list back to SharedPreferences
        val json = gson.toJson(favorites)
        prefs.edit().putString("fav_routes", json).apply()

        // 4. Refresh the UI so the chip disappears from the screen
        updateFavoritesUI()

        Toast.makeText(this, "Favorite deleted successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun showStopsPopup(context: Context, train: SearchResult) {
        val builder = android.app.AlertDialog.Builder(context)

        val headerColor = when {
            train.id == "12" || train.id == "15" -> "#FB8C00"
            train.id.startsWith("15") || train.id.startsWith("B5") -> "#2E7D32"
            train.id.startsWith("B1") || train.id.startsWith("10") -> "#1976D2"
            else -> "#7B1FA2"
        }

        val titleView = android.widget.TextView(context).apply {
            val label = if (isHolidayTrain(train.id)) " (Fridays/Holidays)" else ""
            text = "Train ${train.id}$label"
            textSize = 20f
            setPadding(40, 40, 40, 40)
            setTextColor(android.graphics.Color.WHITE)
            setBackgroundColor(android.graphics.Color.parseColor(headerColor))
        }
        builder.setCustomTitle(titleView)

        // Using .name here to fix your 'unresolved station' error
        val stopsList = train.stops.joinToString("\n") { " ▪  ${it.name} : ${it.time}" }

        val messageView = android.widget.TextView(context).apply {
            text = stopsList
            textSize = 16f
            setPadding(40, 40, 40, 40)
            // FIX: Using setLineSpacing instead of = assignment
            setLineSpacing(0f, 1.4f)
            setTextColor(android.graphics.Color.DKGRAY)
        }

        val scrollView = android.widget.ScrollView(context)
        scrollView.addView(messageView)

        builder.setView(scrollView)
        builder.setPositiveButton("Close", null)
        builder.show()
    }

}

fun isHolidayTrain(id: String): Boolean {
    return id.contains("H")}
// --- RECYCLERVIEW ADAPTER ---
class TrainAdapter(
    private val list: List<SearchResult>,
    private val onItemClick: (SearchResult) -> Unit, // Add this
    private val onLongClick: (SearchResult) -> Unit
) : RecyclerView.Adapter<TrainAdapter.VH>() {


    // Updated ViewHolder to match your custom item_train.xml
    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val layout: LinearLayout = v.findViewById(R.id.rowLayout)
        val tvId: TextView = v.findViewById(R.id.tvTrainId)
        val tvDep: TextView = v.findViewById(R.id.tvDepTime)
        val tvArr: TextView = v.findViewById(R.id.tvArrTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_train, parent, false))


    // --- THE UPDATED FUNCTION ---
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]

        // 1. Determine if it's a holiday train
        val isHoliday = isHolidayTrain(item.id)

        // 2. Set the stroke (border) color
        val strokeColor = if (isHoliday) "#D32F2F" else "#00000000" // Red for Holidays

        holder.tvId.text = "Train ${item.id}"
        holder.tvDep.text = item.depTime
        holder.tvArr.text = item.arrTime

        val baseColor = when {
            item.id == "12" || item.id == "15" -> "#FFF3E0" // Light Orange (Bouira)
            item.id.startsWith("15") || item.id.startsWith("B5") -> "#E8F5E9" // Light Green (Zéralda)
            item.id.startsWith("B1") -> "#E3F2FD" // Light Blue (El Affroun)
            item.id.startsWith("10") || item.id.startsWith("119") ||
                    item.id.startsWith("121") || item.id.startsWith("B13") ||
                    item.id.startsWith("B14") -> "#F3E5F5" // Light Purple/Yellow (Tizi Ouzou)
            position % 2 == 0 -> "#F5F5F5" // Grey
            else -> "#FFFFFF" // White
        }

        // 3. Apply Background and Stroke
        val background = android.graphics.drawable.GradientDrawable().apply {
            setColor(android.graphics.Color.parseColor(baseColor))
            if (isHoliday) {
                setStroke(5, android.graphics.Color.parseColor(strokeColor)) // 5px red border
            } else {
                setStroke(0, android.graphics.Color.TRANSPARENT)
            }
            cornerRadius = 8f // Optional: matches card-like rounded corners
        }

        holder.layout.background = background

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }
    }

    override fun getItemCount() = list.size
}

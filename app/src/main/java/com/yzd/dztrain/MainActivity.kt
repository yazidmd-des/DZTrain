package com.yzd.dztrain

import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import android.widget.ImageButton
import android.widget.Toast
import android.content.Context
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*

// --- DATA MODELS ---
data class Stop (val name: String, val time: String) : Serializable
data class Train(val id: String, val stops: List<Stop>) : Serializable

data class FavoriteRoute(
    val from: String,
    val to: String,
    val time: String
) : Serializable

// --- THE COMPLETE DATASET (Extracted from Images) ---
object TrainRepository {

    val allTrains = listOf(

        // --- ALGER -> THENIA (East Line) ---
        Train("27", listOf(Stop("Alger", "06:30"), Stop("Agha", "06:33"), Stop("Ateliers", "06:36"), Stop("Hussein Dey", "06:40"), Stop("Caroubier", "06:43"), Stop("El Harrach", "06:46"), Stop("Oued Smar", "06:51"), Stop("Bab Ezzouar", "06:54"), Stop("Dar El Beida", "06:57"), Stop("Rouiba", "07:02"), Stop("Rouiba Ind", "07:04"), Stop("Reghaia Ind", "07:06"), Stop("Reghaia", "07:07"), Stop("Boudouaou", "07:13"), Stop("Corso", "07:16"), Stop("Boumerdes", "07:20"), Stop("Tidjelabine", "07:24"), Stop("Thenia", "07:29"))),
        Train("B152/153", listOf(Stop("Alger", "06:20"), Stop("Agha", "06:23"), Stop("Ateliers", "06:26"), Stop("El Harrach", "06:36"), Stop("Bab Ezzouar", "06:44"), Stop("Dar El Beida", "06:47"), Stop("Rouiba", "06:52"), Stop("Reghaia", "06:57"), Stop("Boudouaou", "07:03"), Stop("Boumerdes", "07:10"), Stop("Thenia", "07:19"))),

        Train("33", listOf(Stop("Alger", "08:30"), Stop("Agha", "08:33"), Stop("Ateliers", "08:36"), Stop("Hussein Dey", "08:40"), Stop("Caroubier", "08:43"), Stop("El Harrach", "08:46"), Stop("Oued Smar", "08:51"), Stop("Bab Ezzouar", "08:54"), Stop("Dar El Beida", "08:57"), Stop("Rouiba", "09:02"), Stop("Rouiba Ind", "09:04"), Stop("Reghaia Ind", "09:06"), Stop("Reghaia", "09:07"), Stop("Boudouaou", "09:13"), Stop("Corso", "09:16"), Stop("Boumerdes", "09:20"), Stop("Tidjelabine", "09:24"), Stop("Thenia", "09:29"))),
        Train("B506/101", listOf(Stop("Alger", "07:30"), Stop("Agha", "07:33"), Stop("Ateliers", "07:37"), Stop("Hussein Dey", "07:41"), Stop("Caroubier", "07:44"), Stop("El Harrach", "07:47"), Stop("Oued Smar", "07:53"), Stop("Bab Ezzouar", "07:56"), Stop("Dar El Beida", "08:00"), Stop("Rouiba", "08:05"), Stop("Rouiba Ind", "08:07"), Stop("Reghaia Ind", "08:09"), Stop("Reghaia", "08:10"), Stop("Boudouaou", "08:13"), Stop("Boumerdes", "08:20"), Stop("Tidjelabine", "08:24"), Stop("Thenia", "08:29"))),
        Train("35", listOf(Stop("Alger", "09:45"), Stop("Agha", "09:48"), Stop("Ateliers", "09:51"), Stop("Hussein Dey", "09:55"), Stop("Caroubier", "09:58"), Stop("El Harrach", "10:01"), Stop("Oued Smar", "10:06"), Stop("Bab Ezzouar", "10:09"), Stop("Dar El Beida", "10:12"), Stop("Rouiba", "10:17"), Stop("Reghaia", "10:22"), Stop("Boudouaou", "10:28"), Stop("Corso", "10:31"), Stop("Boumerdes", "10:35"), Stop("Tidjelabine", "10:39"), Stop("Thenia", "10:44"))),
        Train("41", listOf(Stop("Alger", "11:20"), Stop("Agha", "11:23"), Stop("Ateliers", "11:26"), Stop("Hussein Dey", "11:30"), Stop("Caroubier", "11:33"), Stop("El Harrach", "11:36"), Stop("Oued Smar", "11:41"), Stop("Bab Ezzouar", "11:44"), Stop("Dar El Beida", "11:47"), Stop("Rouiba", "11:52"), Stop("Reghaia", "11:57"), Stop("Boudouaou", "12:03"), Stop("Corso", "12:06"), Stop("Boumerdes", "12:10"), Stop("Tidjelabine", "12:14"), Stop("Thenia", "12:19"))),
        Train("47", listOf(Stop("Alger", "12:35"), Stop("Agha", "12:38"), Stop("Ateliers", "12:42"), Stop("Hussein Dey", "12:46"), Stop("Caroubier", "12:49"), Stop("El Harrach", "12:52"), Stop("Oued Smar", "12:58"), Stop("Bab Ezzouar", "13:01"), Stop("Dar El Beida", "13:05"), Stop("Rouiba", "13:10"), Stop("Reghaia", "13:15"), Stop("Boudouaou", "13:21"), Stop("Corso", "13:24"), Stop("Boumerdes", "13:28"), Stop("Tidjelabine", "13:32"), Stop("Thenia", "13:37"))),
        Train("51", listOf(Stop("Alger", "14:00"), Stop("Agha", "14:03"), Stop("Ateliers", "14:06"), Stop("Hussein Dey", "14:10"), Stop("Caroubier", "14:13"), Stop("El Harrach", "14:16"), Stop("Oued Smar", "14:21"), Stop("Bab Ezzouar", "14:24"), Stop("Dar El Beida", "14:27"), Stop("Rouiba", "14:32"), Stop("Rouiba Ind", "14:34"), Stop("Reghaia Ind", "14:36"), Stop("Reghaia", "14:37"), Stop("Boudouaou", "14:43"), Stop("Corso", "14:46"), Stop("Boumerdes", "14:50"), Stop("Tidjelabine", "14:54"), Stop("Thenia", "14:59"))),
        Train("B154/155", listOf(Stop("Alger", "15:05"), Stop("Agha", "15:08"), Stop("Ateliers", "15:12"), Stop("Hussein Dey", "15:16"), Stop("Caroubier", "15:19"), Stop("El Harrach", "15:22"), Stop("Oued Smar", "15:28"), Stop("Bab Ezzouar", "15:31"), Stop("Dar El Beida", "15:35"), Stop("Rouiba", "15:40"), Stop("Rouiba Ind", "15:42"), Stop("Reghaia Ind", "15:44"), Stop("Reghaia", "15:45"), Stop("Boudouaou", "15:51"), Stop("Corso", "15:54"), Stop("Boumerdes", "15:58"), Stop("Tidjelabine", "16:02"), Stop("Thenia", "16:07"))),
        Train("57", listOf(Stop("Alger", "16:05"), Stop("Agha", "16:08"), Stop("Ateliers", "16:11"), Stop("Hussein Dey", "16:15"), Stop("Caroubier", "16:18"), Stop("El Harrach", "16:21"), Stop("Oued Smar", "16:26"), Stop("Bab Ezzouar", "16:29"), Stop("Dar El Beida", "16:32"), Stop("Rouiba", "16:37"), Stop("Rouiba Ind", "16:39"), Stop("Reghaia Ind", "16:41"), Stop("Reghaia", "16:42"), Stop("Boudouaou", "16:48"), Stop("Corso", "16:51"), Stop("Boumerdes", "16:55"), Stop("Tidjelabine", "16:59"), Stop("Thenia", "17:04"))),
        Train("61", listOf(Stop("Alger", "17:05"), Stop("Agha", "17:08"), Stop("Ateliers", "17:12"), Stop("Hussein Dey", "17:16"), Stop("Caroubier", "17:19"), Stop("El Harrach", "17:22"), Stop("Oued Smar", "17:28"), Stop("Bab Ezzouar", "17:31"), Stop("Dar El Beida", "17:35"), Stop("Rouiba", "17:40"), Stop("Rouiba Ind", "17:42"), Stop("Reghaia Ind", "17:44"), Stop("Reghaia", "17:45"), Stop("Boudouaou", "17:51"), Stop("Corso", "17:54"), Stop("Boumerdes", "17:58"), Stop("Tidjelabine", "18:02"), Stop("Thenia", "18:07"))),
        Train("67", listOf(Stop("Alger", "18:05"), Stop("Agha", "18:08"), Stop("Ateliers", "18:12"), Stop("Hussein Dey", "18:16"), Stop("Caroubier", "18:19"), Stop("El Harrach", "18:22"), Stop("Oued Smar", "18:28"), Stop("Bab Ezzouar", "18:31"), Stop("Dar El Beida", "18:35"), Stop("Rouiba", "18:40"), Stop("Reghaia", "18:45"), Stop("Boudouaou", "18:51"), Stop("Corso", "18:54"), Stop("Boumerdes", "18:58"), Stop("Tidjelabine", "19:02"), Stop("Thenia", "19:07"))),

        Train("71", listOf(Stop("Alger", "17:33"), Stop("El Harrach", "17:44"), Stop("Dar El Beida", "17:53"), Stop("Rouiba", "17:57"), Stop("Reghaia", "18:02"), Stop("Boumerdes", "18:13"), Stop("Thenia", "18:21"))),
        Train("73-H", listOf(Stop("Alger", "17:40"), Stop("Agha", "17:43"), Stop("Ateliers", "17:46"), Stop("Hussein Dey", "17:50"), Stop("Caroubier", "17:53"), Stop("El Harrach", "17:56"), Stop("Oued Smar", "18:02"), Stop("Bab Ezzouar", "18:05"), Stop("Dar El Beida", "18:09"), Stop("Rouiba", "18:14"), Stop("Reghaia", "18:19"), Stop("Boudouaou", "18:25"), Stop("Corso", "18:28"), Stop("Boumerdes", "18:32"), Stop("Tidjelabine", "18:36"), Stop("Thenia", "18:41"))),
        Train("75", listOf(Stop("Alger", "18:05"), Stop("Agha", "18:08"), Stop("Ateliers", "18:12"), Stop("Hussein Dey", "18:16"), Stop("Caroubier", "18:19"), Stop("El Harrach", "18:22"), Stop("Oued Smar", "18:28"), Stop("Bab Ezzouar", "18:31"), Stop("Dar El Beida", "18:35"), Stop("Rouiba", "18:40"), Stop("Reghaia", "18:45"), Stop("Boudouaou", "18:51"), Stop("Corso", "18:54"), Stop("Boumerdes", "18:58"), Stop("Tidjelabine", "19:02"), Stop("Thenia", "19:07"))),
        Train("75-H", listOf(Stop("Alger", "18:30"), Stop("Agha", "18:33"), Stop("Ateliers", "18:36"), Stop("Hussein Dey", "18:40"), Stop("Caroubier", "18:43"), Stop("El Harrach", "18:46"), Stop("Oued Smar", "18:51"), Stop("Bab Ezzouar", "18:54"), Stop("Dar El Beida", "18:57"), Stop("Rouiba", "19:02"), Stop("Rouiba Ind", "19:04"), Stop("Reghaia Ind", "19:06"), Stop("Reghaia", "19:07"), Stop("Boudouaou", "19:13"), Stop("Corso", "19:16"), Stop("Boumerdes", "19:20"), Stop("Tidjelabine", "19:24"), Stop("Thenia", "19:29"))),
        Train("79", listOf(Stop("Alger", "19:00"), Stop("Agha", "19:03"), Stop("Ateliers", "19:07"), Stop("Hussein Dey", "19:11"), Stop("Caroubier", "19:14"), Stop("El Harrach", "19:17"), Stop("Oued Smar", "19:23"), Stop("Bab Ezzouar", "19:26"), Stop("Dar El Beida", "19:30"), Stop("Rouiba", "19:35"), Stop("Reghaia", "19:40"), Stop("Boudouaou", "19:46"), Stop("Corso", "19:49"), Stop("Boumerdes", "19:53"), Stop("Tidjelabine", "19:57"), Stop("Thenia", "20:02"))),

        // --- THENIA -> ALGER (West Line) ---
        Train("22", listOf(Stop("Thenia", "06:00"), Stop("Tidjelabine", "06:04"), Stop("Boumerdes", "06:08"), Stop("Corso", "06:12"), Stop("Boudouaou", "06:15"), Stop("Reghaia", "06:21"), Stop("Reghaia Ind", "06:23"), Stop("Rouiba Ind", "06:25"), Stop("Rouiba", "06:26"), Stop("Dar El Beida", "06:32"), Stop("Bab Ezzouar", "06:35"), Stop("Oued Smar", "06:39"), Stop("El Harrach", "06:44"), Stop("Caroubier", "06:47"), Stop("Hussein Dey", "06:50"), Stop("Ateliers", "06:54"), Stop("Agha", "06:57"), Stop("Alger", "07:02"))),
        Train("28", listOf(Stop("Thenia", "06:45"), Stop("Tidjelabine", "06:49"), Stop("Boumerdes", "06:53"), Stop("Corso", "06:58"), Stop("Boudouaou", "07:01"), Stop("Reghaia", "07:07"), Stop("Reghaia Ind", "07:09"), Stop("Rouiba Ind", "07:11"), Stop("Rouiba", "07:12"), Stop("Dar El Beida", "07:18"), Stop("Bab Ezzouar", "07:21"), Stop("Oued Smar", "07:24"), Stop("El Harrach", "07:29"), Stop("Caroubier", "07:32"), Stop("Hussein Dey", "07:35"), Stop("Ateliers", "07:39"), Stop("Agha", "07:42"), Stop("Alger", "07:47"))),
        Train("34", listOf(Stop("Thenia", "08:35"), Stop("Tidjelabine", "08:40"), Stop("Boumerdes", "08:45"), Stop("Corso", "08:50"), Stop("Boudouaou", "08:55"), Stop("Reghaia", "09:00"), Stop("Reghaia Ind", "09:03"), Stop("Rouiba Ind", "09:04"), Stop("Rouiba", "09:06"), Stop("Dar El Beida", "09:12"), Stop("Bab Ezzouar", "09:15"), Stop("Oued Smar", "09:19"), Stop("El Harrach", "08:48"), Stop("Caroubier", "08:51"), Stop("Hussein Dey", "08:54"), Stop("Ateliers", "08:58"), Stop("Agha", "09:01"), Stop("Alger", "09:05"))),
        Train("B126/127", listOf(Stop("Thenia", "08:05"), Stop("Tidjelabine", "08:09"), Stop("Boumerdes", "08:13"), Stop("Corso", "08:17"), Stop("Boudouaou", "08:20"), Stop("Reghaia", "08:26"), Stop("Reghaia Ind", "08:28"), Stop("Rouiba Ind", "08:30"), Stop("Rouiba", "08:31"), Stop("Dar El Beida", "08:37"), Stop("Bab Ezzouar", "08:40"), Stop("Oued Smar", "08:43"))),
        Train("40", listOf(Stop("Thenia", "09:25"), Stop("Tidjelabine", "09:29"), Stop("Boumerdes", "09:33"), Stop("Corso", "09:37"), Stop("Boudouaou", "09:40"), Stop("Reghaia", "09:46"), Stop("Rouiba", "09:51"), Stop("Dar El Beida", "09:57"), Stop("Bab Ezzouar", "10:00"), Stop("Oued Smar", "10:03"), Stop("El Harrach", "10:08"), Stop("Caroubier", "10:12"), Stop("Hussein Dey", "10:15"), Stop("Ateliers", "10:19"), Stop("Agha", "10:22"), Stop("Alger", "10:27"))),
        Train("B102/507", listOf(Stop("Thenia", "10:35"), Stop("Tidjelabine", "10:39"), Stop("Boumerdes", "10:43"), Stop("Corso", "10:47"), Stop("Boudouaou", "10:50"), Stop("Reghaia", "10:56"), Stop("Rouiba", "11:01"), Stop("Dar El Beida", "11:07"), Stop("Bab Ezzouar", "11:10"), Stop("Oued Smar", "11:13"))),
        Train("44-H", listOf(Stop("Thenia", "10:20"), Stop("Tidjelabine", "10:25"), Stop("Boumerdes", "10:30"), Stop("Corso", "10:35"), Stop("Boudouaou", "10:40"), Stop("Reghaia", "10:44"), Stop("Rouiba Ind", "10:48"), Stop("Rouiba", "10:50"), Stop("Dar El Beida", "10:54"), Stop("Bab Ezzouar", "10:57"), Stop("Oued Smar", "11:01"), Stop("El Harrach", "11:23"), Stop("Hussein Dey", "11:27"), Stop("Ateliers", "11:30"), Stop("Agha", "11:34"), Stop("Alger", "11:42"))),
        Train("46", listOf(Stop("Thenia", "11:00"), Stop("Tidjelabine", "11:04"), Stop("Boumerdes", "11:08"), Stop("Corso", "11:12"), Stop("Boudouaou", "11:15"), Stop("Reghaia", "11:21"), Stop("Rouiba", "11:26"), Stop("Dar El Beida", "11:32"), Stop("Bab Ezzouar", "11:35"), Stop("Oued Smar", "11:39"), Stop("El Harrach", "11:44"), Stop("Caroubier", "11:47"), Stop("Hussein Dey", "11:50"), Stop("Ateliers", "11:54"), Stop("Agha", "11:57"), Stop("Alger", "12:02"))),
        Train("50", listOf(Stop("Thenia", "12:00"), Stop("Tidjelabine", "12:04"), Stop("Boumerdes", "12:08"), Stop("Corso", "12:12"), Stop("Boudouaou", "12:15"), Stop("Reghaia", "12:21"), Stop("Rouiba", "12:26"), Stop("Dar El Beida", "12:32"), Stop("Bab Ezzouar", "12:35"), Stop("Oued Smar", "12:39"), Stop("El Harrach", "12:44"), Stop("Caroubier", "12:48"), Stop("Hussein Dey", "12:51"), Stop("Ateliers", "12:55"), Stop("Agha", "12:58"), Stop("Alger", "13:03"))),
        Train("56", listOf(Stop("Thenia", "13:30"), Stop("Tidjelabine", "13:34"), Stop("Boumerdes", "13:38"), Stop("Corso", "13:42"), Stop("Boudouaou", "13:45"), Stop("Reghaia", "13:51"), Stop("Rouiba", "13:56"), Stop("Dar El Beida", "14:02"), Stop("Bab Ezzouar", "14:05"), Stop("Oued Smar", "14:08"), Stop("El Harrach", "14:13"), Stop("Caroubier", "14:17"), Stop("Hussein Dey", "14:20"), Stop("Ateliers", "14:24"), Stop("Agha", "14:27"), Stop("Alger", "14:32"))),
        Train("58", listOf(Stop("Thenia", "14:20"), Stop("Tidjelabine", "14:24"), Stop("Boumerdes", "14:28"), Stop("Corso", "14:32"), Stop("Boudouaou", "14:35"), Stop("Reghaia", "14:41"), Stop("Rouiba", "14:46"), Stop("Dar El Beida", "14:52"), Stop("Bab Ezzouar", "14:55"), Stop("Oued Smar", "14:58"), Stop("El Harrach", "15:03"), Stop("Caroubier", "15:07"), Stop("Hussein Dey", "15:10"), Stop("Ateliers", "15:14"), Stop("Agha", "15:17"), Stop("Alger", "15:20"))),
        Train("62", listOf(Stop("Thenia", "16:30"), Stop("Tidjelabine", "16:34"), Stop("Boumerdes", "16:38"), Stop("Corso", "16:42"), Stop("Boudouaou", "16:45"), Stop("Reghaia", "16:51"), Stop("Reghaia Ind", "16:53"), Stop("Rouiba", "16:56"), Stop("Dar El Beida", "17:02"), Stop("Bab Ezzouar", "17:05"), Stop("Oued Smar", "17:08"), Stop("El Harrach", "17:13"), Stop("Caroubier", "17:17"), Stop("Hussein Dey", "17:20"), Stop("Ateliers", "17:24"), Stop("Agha", "17:27"), Stop("Alger", "17:32"))),
        Train("B128/129", listOf(Stop("Thenia", "16:00"), Stop("Tidjelabine", "16:05"), Stop("Boumerdes", "16:10"), Stop("Corso", "16:15"), Stop("Boudouaou", "16:20"), Stop("Reghaia", "16:25"), Stop("Reghaia Ind", "16:27"), Stop("Rouiba Ind", "16:29"), Stop("Rouiba", "16:31"), Stop("Dar El Beida", "16:37"), Stop("Bab Ezzouar", "16:40"), Stop("Oued Smar", "16:44"))),
        Train("66", listOf(Stop("Thenia", "17:13"), Stop("Boumerdes", "17:19"), Stop("Reghaia", "17:29"), Stop("Rouiba", "17:35"), Stop("Dar El Beida", "17:39"), Stop("Bab Ezzouar", "18:20"), Stop("Oued Smar", "18:23"), Stop("El Harrach", "18:28"), Stop("Caroubier", "18:32"), Stop("Hussein Dey", "18:35"), Stop("Ateliers", "18:39"), Stop("Agha", "18:42"), Stop("Alger", "18:46"))),
        Train("74", listOf(Stop("Thenia", "17:45"), Stop("Tidjelabine", "17:49"), Stop("Boumerdes", "17:53"), Stop("Corso", "17:57"), Stop("Boudouaou", "18:00"), Stop("Reghaia", "18:06"), Stop("Rouiba", "18:11"), Stop("Dar El Beida", "18:17"), Stop("Bab Ezzouar", "18:20"), Stop("Oued Smar", "18:23"), Stop("El Harrach", "18:28"), Stop("Caroubier", "18:32"), Stop("Hussein Dey", "18:35"), Stop("Ateliers", "18:39"), Stop("Agha", "18:42"), Stop("Alger", "18:46"))),
        Train("78", listOf(Stop("Thenia", "18:50"), Stop("Tidjelabine", "18:54"), Stop("Boumerdes", "18:58"), Stop("Corso", "19:02"), Stop("Boudouaou", "19:05"), Stop("Reghaia", "19:11"), Stop("Rouiba", "19:16"), Stop("Dar El Beida", "19:22"), Stop("Bab Ezzouar", "19:25"), Stop("Oued Smar", "19:28"), Stop("El Harrach", "19:33"), Stop("Caroubier", "19:36"), Stop("Hussein Dey", "19:39"), Stop("Ateliers", "19:43"), Stop("Agha", "19:46"), Stop("Alger", "19:51"))),

        // --- ALGER -> EL AFFROUN (West Line) ---
        Train("1027", listOf(Stop("Alger", "06:20"), Stop("Agha", "06:23"), Stop("Ateliers", "06:27"), Stop("Caroubier", "06:31"), Stop("El Harrach", "06:42"), Stop("Gué de Cne", "06:45"), Stop("Ain Naadja", "06:49"), Stop("Birtouta", "06:54"), Stop("Boufarik", "07:02"), Stop("B.Mered", "07:08"), Stop("Blida", "07:14"), Stop("Mouzaia", "07:25"), Stop("El Affroun", "07:30"))),
        Train("1029", listOf(Stop("Alger", "07:10"), Stop("Agha", "07:13"), Stop("Ateliers", "07:17"), Stop("H.Dey", "07:21"), Stop("Caroubier", "07:24"), Stop("El Harrach", "07:27"), Stop("Gué de Cne", "07:32"), Stop("Ain Naadja", "07:35"), Stop("Baba Ali", "07:39"), Stop("Birtouta", "07:44"), Stop("Boufarik", "07:52"), Stop("B.Mered", "07:58"), Stop("Blida", "08:04"), Stop("Chiffa", "08:11"), Stop("Mouzaia", "08:15"), Stop("El Affroun", "08:20"))),
        Train("1031", listOf(Stop("Alger", "08:30"), Stop("Agha", "08:33"), Stop("Ateliers", "08:37"), Stop("H.Dey", "08:41"), Stop("Caroubier", "08:44"), Stop("El Harrach", "08:47"), Stop("Gué de Cne", "08:52"), Stop("Ain Naadja", "08:55"), Stop("Baba Ali", "08:59"), Stop("Birtouta", "09:04"), Stop("Boufarik", "09:12"), Stop("B.Mered", "09:18"), Stop("Blida", "09:24"), Stop("Chiffa", "09:31"), Stop("Mouzaia", "09:35"), Stop("El Affroun", "09:40"))),
        Train("1035-H", listOf(Stop("Alger", "09:00"), Stop("Agha", "09:03"), Stop("Ateliers", "09:07"), Stop("H.Dey", "09:11"), Stop("Caroubier", "09:14"), Stop("El Harrach", "09:17"), Stop("Gué de Cne", "09:22"), Stop("Ain Naadja", "09:25"), Stop("Baba Ali", "09:29"), Stop("Birtouta", "09:34"), Stop("Boufarik", "09:42"), Stop("B.Mered", "09:48"), Stop("Blida", "09:54"), Stop("Chiffa", "10:01"), Stop("Mouzaia", "10:05"), Stop("El Affroun", "10:10"))),
        Train("1037", listOf(Stop("Alger", "09:55"), Stop("Agha", "09:58"), Stop("Ateliers", "10:02"), Stop("H.Dey", "10:06"), Stop("Caroubier", "10:09"), Stop("El Harrach", "10:12"), Stop("Gué de Cne", "10:17"), Stop("Ain Naadja", "10:20"), Stop("Baba Ali", "10:24"), Stop("Birtouta", "10:29"), Stop("Boufarik", "10:38"), Stop("B.Mered", "10:44"), Stop("Blida", "10:50"), Stop("Chiffa", "10:57"), Stop("Mouzaia", "11:01"), Stop("El Affroun", "11:06"))),
        Train("1037-H", listOf(Stop("Alger", "10:20"), Stop("Agha", "10:23"), Stop("Ateliers", "10:27"), Stop("H.Dey", "10:31"), Stop("Caroubier", "10:34"), Stop("El Harrach", "10:37"), Stop("Gué de Cne", "10:42"), Stop("Ain Naadja", "10:45"), Stop("Baba Ali", "10:49"), Stop("Birtouta", "10:54"), Stop("Boufarik", "11:02"), Stop("B.Mered", "11:08"), Stop("Blida", "11:14"), Stop("Chiffa", "11:21"), Stop("Mouzaia", "11:25"), Stop("El Affroun", "11:30"))),
        Train("1043", listOf(Stop("Alger", "11:30"), Stop("Agha", "11:33"), Stop("Ateliers", "11:34"), Stop("H.Dey", "11:41"), Stop("Caroubier", "11:44"), Stop("El Harrach", "11:47"), Stop("Gué de Cne", "11:52"), Stop("Ain Naadja", "11:55"), Stop("Baba Ali", "11:59"), Stop("Birtouta", "12:04"), Stop("Boufarik", "12:12"), Stop("B.Mered", "12:18"), Stop("Blida", "12:24"), Stop("Chiffa", "12:31"), Stop("Mouzaia", "12:35"), Stop("El Affroun", "12:40"))),
        Train("1045", listOf(Stop("Alger", "12:30"), Stop("Agha", "12:33"), Stop("Ateliers", "12:34"), Stop("H.Dey", "12:41"), Stop("Caroubier", "12:44"), Stop("El Harrach", "12:47"), Stop("Gué de Cne", "12:52"), Stop("Ain Naadja", "12:55"), Stop("Baba Ali", "12:59"), Stop("Birtouta", "13:04"), Stop("Boufarik", "13:12"), Stop("B.Mered", "13:18"), Stop("Blida", "13:24"), Stop("Chiffa", "13:31"), Stop("Mouzaia", "13:35"), Stop("El Affroun", "13:40"))),
        Train("1049-H", listOf(Stop("Alger", "13:20"), Stop("Agha", "13:23"), Stop("Ateliers", "13:27"), Stop("H.Dey", "13:31"), Stop("Caroubier", "13:34"), Stop("El Harrach", "13:37"), Stop("Gué de Cne", "13:42"), Stop("Ain Naadja", "13:45"), Stop("Baba Ali", "13:49"), Stop("Birtouta", "13:54"), Stop("Boufarik", "14:02"), Stop("B.Mered", "14:08"), Stop("Blida", "14:14"), Stop("Chiffa", "14:21"), Stop("Mouzaia", "14:25"), Stop("El Affroun", "14:30"))),
        Train("1051", listOf(Stop("Alger", "14:10"), Stop("Agha", "14:13"), Stop("Ateliers", "14:17"), Stop("H.Dey", "14:21"), Stop("Caroubier", "14:24"), Stop("El Harrach", "14:27"), Stop("Gué de Cne", "14:32"), Stop("Ain Naadja", "14:35"), Stop("Baba Ali", "14:39"), Stop("Birtouta", "14:44"), Stop("Boufarik", "14:52"), Stop("B.Mered", "14:58"), Stop("Blida", "15:04"), Stop("Chiffa", "15:11"), Stop("Mouzaia", "15:15"), Stop("El Affroun", "15:20"))),
        Train("1051-H", listOf(Stop("Alger", "14:30"), Stop("Agha", "14:33"), Stop("Ateliers", "14:34"), Stop("H.Dey", "14:41"), Stop("Caroubier", "14:44"), Stop("El Harrach", "14:47"), Stop("Gué de Cne", "14:52"), Stop("Ain Naadja", "14:55"), Stop("Baba Ali", "14:59"), Stop("Birtouta", "15:04"), Stop("Boufarik", "15:12"), Stop("B.Mered", "15:18"), Stop("Blida", "15:24"), Stop("Chiffa", "15:31"), Stop("Mouzaia", "15:40"), Stop("El Affroun", "15:40"))),
        Train("1053", listOf(Stop("Alger", "15:10"), Stop("Agha", "15:13"), Stop("Ateliers", "15:17"), Stop("H.Dey", "15:21"), Stop("Caroubier", "15:24"), Stop("El Harrach", "15:27"), Stop("Gué de Cne", "15:32"), Stop("Ain Naadja", "15:35"), Stop("Baba Ali", "15:39"), Stop("Birtouta", "15:44"), Stop("Boufarik", "15:52"), Stop("B.Mered", "15:58"), Stop("Blida", "16:04"), Stop("Chiffa", "16:11"), Stop("Mouzaia", "16:15"), Stop("El Affroun", "16:20"))),
        Train("1057", listOf(Stop("Alger", "16:10"), Stop("Agha", "16:13"), Stop("Ateliers", "16:17"), Stop("H.Dey", "16:21"), Stop("Caroubier", "16:24"), Stop("El Harrach", "16:27"), Stop("Gué de Cne", "16:32"), Stop("Ain Naadja", "16:35"), Stop("Baba Ali", "16:39"), Stop("Birtouta", "16:44"), Stop("Boufarik", "16:52"), Stop("B.Mered", "16:58"), Stop("Blida", "17:04"), Stop("Chiffa", "17:11"), Stop("Mouzaia", "17:15"), Stop("El Affroun", "17:20"))),
        Train("1061", listOf(Stop("Alger", "17:10"), Stop("Agha", "17:13"), Stop("Ateliers", "17:17"), Stop("H.Dey", "17:21"), Stop("Caroubier", "17:24"), Stop("El Harrach", "17:27"), Stop("Gué de Cne", "17:32"), Stop("Ain Naadja", "17:35"), Stop("Baba Ali", "17:39"), Stop("Birtouta", "17:44"), Stop("Boufarik", "17:52"), Stop("B.Mered", "17:58"), Stop("Blida", "18:04"), Stop("Chiffa", "18:11"), Stop("Mouzaia", "18:15"), Stop("El Affroun", "18:20"))),
        Train("1063-H", listOf(Stop("Alger", "17:35"), Stop("Agha", "17:38"), Stop("Ateliers", "17:41"), Stop("H.Dey", "17:45"), Stop("Caroubier", "17:48"), Stop("El Harrach", "17:51"), Stop("Gué de Cne", "17:56"), Stop("Ain Naadja", "17:59"), Stop("Baba Ali", "18:03"), Stop("Birtouta", "18:08"), Stop("Boufarik", "18:16"), Stop("B.Mered", "18:22"), Stop("Blida", "18:28"), Stop("Chiffa", "18:35"), Stop("Mouzaia", "18:39"), Stop("El Affroun", "18:44"))),
        Train("1065", listOf(Stop("Alger", "18:30"), Stop("Agha", "18:33"), Stop("Ateliers", "18:34"), Stop("H.Dey", "18:41"), Stop("Caroubier", "18:44"), Stop("El Harrach", "18:47"), Stop("Gué de Cne", "18:52"), Stop("Ain Naadja", "18:55"), Stop("Baba Ali", "18:59"), Stop("Birtouta", "19:04"), Stop("Boufarik", "19:12"), Stop("B.Mered", "19:18"), Stop("Blida", "19:24"), Stop("Chiffa", "19:31"), Stop("Mouzaia", "19:35"), Stop("El Affroun", "19:40"))),
        Train("1067", listOf(Stop("Alger", "18:50"), Stop("Agha", "18:53"), Stop("Ateliers", "18:57"), Stop("H.Dey", "19:01"), Stop("Caroubier", "19:04"), Stop("El Harrach", "19:07"), Stop("Gué de Cne", "19:12"), Stop("Ain Naadja", "19:15"), Stop("Baba Ali", "19:19"), Stop("Birtouta", "19:24"), Stop("Boufarik", "19:32"), Stop("B.Mered", "19:38"), Stop("Blida", "19:44"), Stop("Chiffa", "19:51"), Stop("Mouzaia", "19:55"), Stop("El Affroun", "20:00"))),

        // --- EL AFFROUN -> ALGER (West Line Return) ---
        Train("1022", listOf(Stop("El Affroun", "05:40"), Stop("Mouzaia", "05:44"), Stop("Chiffa", "05:48"), Stop("Blida", "05:54"), Stop("B.Méred", "06:01"), Stop("Boufarik", "06:07"), Stop("Birtouta", "06:15"), Stop("Baba Ali", "06:20"), Stop("Ain Naadja", "06:24"), Stop("Gué Che", "06:27"), Stop("El Harrach", "06:31"), Stop("Caroubier", "06:34"), Stop("H.Dey", "06:37"), Stop("LesAteliers", "06:41"), Stop("Agha", "06:44"), Stop("Alger", "06:48"))),
        Train("1024", listOf(Stop("El Affroun", "06:30"), Stop("Mouzaia", "06:34"), Stop("Chiffa", "06:38"), Stop("Blida", "06:44"), Stop("B.Méred", "06:50"), Stop("Boufarik", "06:56"), Stop("Birtouta", "07:04"), Stop("Baba Ali", "07:09"), Stop("Ain Naadja", "07:13"), Stop("Gué Che", "07:16"), Stop("El Harrach", "07:20"), Stop("Caroubier", "07:25"), Stop("H.Dey", "07:28"), Stop("LesAteliers", "07:32"), Stop("Agha", "07:35"), Stop("Alger", "07:39"))),
        Train("1028-H", listOf(Stop("El Affroun", "06:45"), Stop("Mouzaia", "06:49"), Stop("Chiffa", "06:53"), Stop("Blida", "06:59"), Stop("B.Méred", "07:06"), Stop("Boufarik", "07:12"), Stop("Birtouta", "07:20"), Stop("Baba Ali", "07:25"), Stop("Ain Naadja", "07:29"), Stop("Gué Che", "07:32"), Stop("El Harrach", "07:36"), Stop("Caroubier", "07:40"), Stop("H.Dey", "07:43"), Stop("LesAteliers", "07:47"), Stop("Agha", "07:50"), Stop("Alger", "07:56"))),
        Train("1032", listOf(Stop("El Affroun", "08:00"), Stop("Mouzaia", "08:04"), Stop("Chiffa", "08:08"), Stop("Blida", "08:14"), Stop("B.Méred", "08:21"), Stop("Boufarik", "08:27"), Stop("Birtouta", "08:35"), Stop("Baba Ali", "08:40"), Stop("Ain Naadja", "08:44"), Stop("Gué Che", "08:47"), Stop("El Harrach", "08:51"), Stop("Caroubier", "08:54"), Stop("H.Dey", "08:57"), Stop("LesAteliers", "09:01"), Stop("Agha", "09:04"), Stop("Alger", "09:09"))),
        Train("1034", listOf(Stop("El Affroun", "09:15"), Stop("Mouzaia", "09:19"), Stop("Chiffa", "09:23"), Stop("Blida", "09:29"), Stop("B.Méred", "09:36"), Stop("Boufarik", "09:42"), Stop("Birtouta", "09:50"), Stop("Baba Ali", "09:55"), Stop("Ain Naadja", "09:59"), Stop("Gué Che", "10:02"), Stop("El Harrach", "10:06"), Stop("Caroubier", "10:09"), Stop("H.Dey", "10:12"), Stop("LesAteliers", "10:16"), Stop("Agha", "10:19"), Stop("Alger", "10:24"))),
        Train("1036-H", listOf(Stop("El Affroun", "09:30"), Stop("Mouzaia", "09:34"), Stop("Chiffa", "09:38"), Stop("Blida", "09:44"), Stop("B.Méred", "09:50"), Stop("Boufarik", "09:56"), Stop("Birtouta", "10:04"), Stop("Baba Ali", "10:09"), Stop("Ain Naadja", "10:13"), Stop("Gué Che", "10:16"), Stop("El Harrach", "10:20"), Stop("Caroubier", "10:25"), Stop("H.Dey", "10:28"), Stop("LesAteliers", "10:32"), Stop("Agha", "10:35"), Stop("Alger", "10:39"))),
        Train("1038", listOf(Stop("El Affroun", "10:30"), Stop("Mouzaia", "10:34"), Stop("Chiffa", "10:38"), Stop("Blida", "10:44"), Stop("B.Méred", "10:50"), Stop("Boufarik", "10:56"), Stop("Birtouta", "11:04"), Stop("Baba Ali", "11:09"), Stop("Ain Naadja", "11:13"), Stop("Gué Che", "11:16"), Stop("El Harrach", "11:20"), Stop("Caroubier", "11:23"), Stop("H.Dey", "11:26"), Stop("LesAteliers", "11:30"), Stop("Agha", "11:33"), Stop("Alger", "11:37"))),
        Train("1040-H", listOf(Stop("El Affroun", "10:45"), Stop("Mouzaia", "10:49"), Stop("Chiffa", "10:53"), Stop("Blida", "10:59"), Stop("B.Méred", "11:06"), Stop("Boufarik", "11:12"), Stop("Birtouta", "11:20"), Stop("Baba Ali", "11:25"), Stop("Ain Naadja", "11:29"), Stop("Gué Che", "11:32"), Stop("El Harrach", "11:36"), Stop("Caroubier", "11:40"), Stop("H.Dey", "11:43"), Stop("LesAteliers", "11:47"), Stop("Agha", "11:50"), Stop("Alger", "11:55"))),
        Train("1044", listOf(Stop("El Affroun", "12:25"), Stop("Mouzaia", "12:29"), Stop("Chiffa", "12:33"), Stop("Blida", "12:39"), Stop("B.Méred", "12:45"), Stop("Boufarik", "12:51"), Stop("Birtouta", "12:59"), Stop("Baba Ali", "13:04"), Stop("Ain Naadja", "13:08"), Stop("Gué Che", "13:11"), Stop("El Harrach", "13:15"), Stop("Caroubier", "13:18"), Stop("H.Dey", "13:21"), Stop("LesAteliers", "13:25"), Stop("Agha", "13:28"), Stop("Alger", "13:33"))),
        Train("1048", listOf(Stop("El Affroun", "13:30"), Stop("Mouzaia", "13:34"), Stop("Chiffa", "13:38"), Stop("Blida", "13:44"), Stop("B.Méred", "13:50"), Stop("Boufarik", "13:56"), Stop("Birtouta", "14:04"), Stop("Baba Ali", "14:09"), Stop("Ain Naadja", "14:13"), Stop("Gué Che", "14:16"), Stop("El Harrach", "14:20"), Stop("Caroubier", "14:23"), Stop("H.Dey", "14:26"), Stop("LesAteliers", "14:30"), Stop("Agha", "14:33"), Stop("Alger", "14:37"))),
        Train("1052", listOf(Stop("El Affroun", "14:20"), Stop("Mouzaia", "14:24"), Stop("Chiffa", "14:28"), Stop("Blida", "14:34"), Stop("B.Méred", "14:41"), Stop("Boufarik", "14:47"), Stop("Birtouta", "14:55"), Stop("Baba Ali", "15:00"), Stop("Ain Naadja", "15:04"), Stop("Gué Che", "15:07"), Stop("El Harrach", "15:11"), Stop("Caroubier", "15:15"), Stop("H.Dey", "15:18"), Stop("LesAteliers", "15:22"), Stop("Agha", "15:25"), Stop("Alger", "15:30"))),
        Train("1054-H", listOf(Stop("El Affroun", "15:20"), Stop("Mouzaia", "15:24"), Stop("Chiffa", "15:28"), Stop("Blida", "15:34"), Stop("B.Méred", "15:41"), Stop("Boufarik", "15:47"), Stop("Birtouta", "15:55"), Stop("Baba Ali", "16:00"), Stop("Ain Naadja", "16:04"), Stop("Gué Che", "16:07"), Stop("El Harrach", "16:11"), Stop("Caroubier", "16:15"), Stop("H.Dey", "16:18"), Stop("LesAteliers", "16:22"), Stop("Agha", "16:25"), Stop("Alger", "16:30"))),
        Train("1056", listOf(Stop("El Affroun", "15:50"), Stop("Mouzaia", "15:54"), Stop("Chiffa", "15:58"), Stop("Blida", "16:04"), Stop("B.Méred", "16:11"), Stop("Boufarik", "16:17"), Stop("Birtouta", "16:25"), Stop("Baba Ali", "16:31"), Stop("Ain Naadja", "16:35"), Stop("Gué Che", "16:38"), Stop("El Harrach", "16:42"), Stop("Caroubier", "16:46"), Stop("H.Dey", "16:49"), Stop("LesAteliers", "16:53"), Stop("Agha", "16:56"), Stop("Alger", "17:01"))),
        Train("1058-H", listOf(Stop("El Affroun", "16:35"), Stop("Mouzaia", "16:39"), Stop("Chiffa", "16:43"), Stop("Blida", "16:49"), Stop("B.Méred", "16:55"), Stop("Boufarik", "17:01"), Stop("Birtouta", "17:09"), Stop("Baba Ali", "17:14"), Stop("Ain Naadja", "17:18"), Stop("Gué Che", "17:21"), Stop("El Harrach", "17:25"), Stop("Caroubier", "17:28"), Stop("H.Dey", "17:31"), Stop("LesAteliers", "17:35"), Stop("Agha", "17:38"), Stop("Alger", "17:43"))),
        Train("1058", listOf(Stop("El Affroun", "16:50"), Stop("Mouzaia", "16:54"), Stop("Chiffa", "16:58"), Stop("Blida", "17:04"), Stop("B.Méred", "17:11"), Stop("Boufarik", "17:17"), Stop("Birtouta", "17:25"), Stop("Baba Ali", "17:31"), Stop("Ain Naadja", "17:35"), Stop("Gué Che", "17:38"), Stop("El Harrach", "17:42"), Stop("Caroubier", "17:46"), Stop("H.Dey", "17:49"), Stop("LesAteliers", "17:53"), Stop("Agha", "17:56"), Stop("Alger", "18:01"))),
        Train("1066", listOf(Stop("El Affroun", "18:30"), Stop("Mouzaia", "18:34"), Stop("Chiffa", "18:38"), Stop("Blida", "18:44"), Stop("B.Méred", "18:50"), Stop("Boufarik", "18:56"), Stop("Birtouta", "19:04"), Stop("Baba Ali", "19:09"), Stop("Ain Naadja", "19:13"), Stop("Gué Che", "19:16"), Stop("El Harrach", "19:20"), Stop("Caroubier", "19:23"), Stop("H.Dey", "19:26"), Stop("LesAteliers", "19:30"), Stop("Agha", "19:33"), Stop("Alger", "19:37"))),

        // --- AGHA -> ZÉRALDA ---
        Train("1501", listOf(Stop("Agha", "05:10"), Stop("Ateliers", "05:12"), Stop("H.Dey", "05:16"), Stop("Caroubier", "05:19"), Stop("El Harrach", "05:22"), Stop("Gué de Cne", "05:26"), Stop("Ain Naadja", "05:29"), Stop("Baba Ali", "05:33"), Stop("Birtouta", "05:38"), Stop("Tessala El Merdja", "05:43"), Stop("Sidi Abdellah", "05:49"), Stop("Université", "05:52"), Stop("Zéralda", "06:00"))),
        Train("B517", listOf(Stop("Agha", "06:40"), Stop("Ateliers", "06:42"), Stop("H.Dey", "06:46"), Stop("Caroubier", "06:49"), Stop("El Harrach", "06:52"), Stop("Gué de Cne", "06:57"), Stop("Ain Naadja", "07:00"), Stop("Baba Ali", "07:04"), Stop("Birtouta", "07:09"), Stop("Tessala El Merdja", "07:14"), Stop("Sidi Abdellah", "07:20"), Stop("Université", "07:23"), Stop("Zéralda", "07:31"))),
        Train("1505", listOf(Stop("Agha", "07:30"), Stop("Ateliers", "07:32"), Stop("H.Dey", "07:36"), Stop("Caroubier", "07:39"), Stop("El Harrach", "07:42"), Stop("Gué de Cne", "07:47"), Stop("Ain Naadja", "07:50"), Stop("Baba Ali", "07:54"), Stop("Birtouta", "07:59"), Stop("Tessala El Merdja", "08:04"), Stop("Sidi Abdellah", "08:10"), Stop("Université", "08:13"), Stop("Zéralda", "08:21"))),
        Train("1509", listOf(Stop("Agha", "08:40"), Stop("Ateliers", "08:42"), Stop("H.Dey", "08:46"), Stop("Caroubier", "08:49"), Stop("El Harrach", "08:52"), Stop("Gué de Cne", "08:57"), Stop("Ain Naadja", "09:00"), Stop("Baba Ali", "09:04"), Stop("Birtouta", "09:09"), Stop("Tessala El Merdja", "09:14"), Stop("Sidi Abdellah", "09:20"), Stop("Université", "09:23"), Stop("Zéralda", "09:31"))),
        Train("1513", listOf(Stop("Agha", "10:15"), Stop("Ateliers", "10:17"), Stop("H.Dey", "10:21"), Stop("Caroubier", "10:24"), Stop("El Harrach", "10:27"), Stop("Gué de Cne", "10:32"), Stop("Ain Naadja", "10:35"), Stop("Baba Ali", "10:39"), Stop("Birtouta", "10:44"), Stop("Tessala El Merdja", "10:49"), Stop("Sidi Abdellah", "10:55"), Stop("Université", "10:58"), Stop("Zéralda", "11:06"))),
        Train("B102/507", listOf(Stop("Agha", "11:35"), Stop("Ateliers", "11:37"), Stop("H.Dey", "11:41"), Stop("Caroubier", "11:44"), Stop("El Harrach", "11:47"), Stop("Gué de Cne", "11:52"), Stop("Ain Naadja", "11:55"), Stop("Baba Ali", "11:59"), Stop("Birtouta", "12:04"), Stop("Tessala El Merdja", "12:09"), Stop("Sidi Abdellah", "12:15"), Stop("Université", "12:18"), Stop("Zéralda", "12:26"))),
        Train("1515", listOf(Stop("Agha", "11:35"), Stop("Ateliers", "11:37"), Stop("H.Dey", "11:41"), Stop("Caroubier", "11:44"), Stop("El Harrach", "11:47"), Stop("Gué de Cne", "11:52"), Stop("Ain Naadja", "11:55"), Stop("Baba Ali", "11:59"), Stop("Birtouta", "12:04"), Stop("Tessala El Merdja", "12:09"), Stop("Sidi Abdellah", "12:15"), Stop("Université", "12:18"), Stop("Zéralda", "12:26"))),
        Train("1519", listOf(Stop("Agha", "13:10"), Stop("Ateliers", "13:12"), Stop("H.Dey", "13:16"), Stop("Caroubier", "13:19"), Stop("El Harrach", "13:22"), Stop("Gué de Cne", "13:27"), Stop("Ain Naadja", "13:30"), Stop("Baba Ali", "13:34"), Stop("Birtouta", "13:39"), Stop("Tessala El Merdja", "13:44"), Stop("Sidi Abdellah", "13:50"), Stop("Université", "13:53"), Stop("Zéralda", "14:01"))),
        Train("1521", listOf(Stop("Agha", "14:20"), Stop("Ateliers", "14:22"), Stop("H.Dey", "14:26"), Stop("Caroubier", "14:29"), Stop("El Harrach", "14:32"), Stop("Gué de Cne", "14:37"), Stop("Ain Naadja", "14:40"), Stop("Baba Ali", "14:44"), Stop("Birtouta", "14:49"), Stop("Tessala El Merdja", "14:54"), Stop("Sidi Abdellah", "15:00"), Stop("Université", "15:03"), Stop("Zéralda", "15:11"))),
        Train("1523", listOf(Stop("Agha", "15:20"), Stop("Ateliers", "15:22"), Stop("H.Dey", "15:26"), Stop("Caroubier", "15:29"), Stop("El Harrach", "15:32"), Stop("Gué de Cne", "15:37"), Stop("Ain Naadja", "15:40"), Stop("Baba Ali", "15:44"), Stop("Birtouta", "15:49"), Stop("Tessala El Merdja", "15:54"), Stop("Sidi Abdellah", "16:00"), Stop("Université", "16:03"), Stop("Zéralda", "16:11"))),
        Train("1525", listOf(Stop("Agha", "16:25"), Stop("Ateliers", "16:27"), Stop("H.Dey", "16:31"), Stop("Caroubier", "16:34"), Stop("El Harrach", "16:37"), Stop("Gué de Cne", "16:42"), Stop("Ain Naadja", "16:45"), Stop("Baba Ali", "16:49"), Stop("Birtouta", "16:54"), Stop("Tessala El Merdja", "16:59"), Stop("Sidi Abdellah", "17:05"), Stop("Université", "17:08"), Stop("Zéralda", "17:16"))),
        Train("1527", listOf(Stop("Agha", "17:30"), Stop("Ateliers", "17:32"), Stop("H.Dey", "17:36"), Stop("Caroubier", "17:39"), Stop("El Harrach", "17:42"), Stop("Gué de Cne", "17:47"), Stop("Ain Naadja", "17:50"), Stop("Baba Ali", "17:54"), Stop("Birtouta", "17:59"), Stop("Tessala El Merdja", "18:04"), Stop("Sidi Abdellah", "18:10"), Stop("Université", "18:13"), Stop("Zéralda", "18:21"))),
        Train("1529", listOf(Stop("Agha", "18:35"), Stop("Ateliers", "18:37"), Stop("H.Dey", "18:41"), Stop("Caroubier", "18:44"), Stop("El Harrach", "18:47"), Stop("Gué de Cne", "18:52"), Stop("Ain Naadja", "18:55"), Stop("Baba Ali", "18:59"), Stop("Birtouta", "19:04"), Stop("Tessala El Merdja", "19:09"), Stop("Sidi Abdellah", "19:15"), Stop("Université", "19:18"), Stop("Zéralda", "19:26"))),

        // --- ZÉRALDA -> AGHA ---
        Train("1500", listOf(Stop("Zéralda", "06:15"), Stop("Université", "06:23"), Stop("Sidi Abdellah", "06:26"), Stop("Tessala El Merdja", "06:32"), Stop("Birtouta", "06:37"), Stop("Baba Ali", "06:41"), Stop("Ain Naadja", "06:45"), Stop("Gué Cne", "06:48"), Stop("El Harrach", "06:52"), Stop("Caroubier", "06:55"), Stop("H.Dey", "06:58"), Stop("Ateliers", "07:02"), Stop("Agha", "07:05"))),
        Train("1502", listOf(Stop("Zéralda", "07:45"), Stop("Université", "07:53"), Stop("Sidi Abdellah", "07:56"), Stop("Tessala El Merdja", "08:02"), Stop("Birtouta", "08:07"), Stop("Baba Ali", "08:11"), Stop("Ain Naadja", "08:15"), Stop("Gué Cne", "08:18"), Stop("El Harrach", "08:22"), Stop("Caroubier", "08:25"), Stop("H.Dey", "08:28"), Stop("Ateliers", "08:32"), Stop("Agha", "08:35"))),
        Train("B506/101", listOf(Stop("Zéralda", "06:25"), Stop("Université", "06:33"), Stop("Sidi Abdellah", "06:36"), Stop("Tessala El Merdja", "06:42"), Stop("Birtouta", "06:47"), Stop("Baba Ali", "06:51"), Stop("Ain Naadja", "06:55"), Stop("Gué Cne", "06:58"), Stop("El Harrach", "07:02"), Stop("Caroubier", "07:05"), Stop("H.Dey", "07:08"), Stop("Ateliers", "07:12"), Stop("Agha", "07:15"))),
        Train("1504", listOf(Stop("Zéralda", "08:35"), Stop("Université", "08:43"), Stop("Sidi Abdellah", "08:46"), Stop("Tessala El Merdja", "08:52"), Stop("Birtouta", "08:57"), Stop("Baba Ali", "09:01"), Stop("Ain Naadja", "09:05"), Stop("Gué Cne", "09:08"), Stop("El Harrach", "09:12"), Stop("Caroubier", "09:15"), Stop("H.Dey", "09:18"), Stop("Ateliers", "09:22"), Stop("Agha", "09:25"))),
        Train("1508", listOf(Stop("Zéralda", "09:45"), Stop("Université", "09:53"), Stop("Sidi Abdellah", "09:56"), Stop("Tessala El Merdja", "10:02"), Stop("Birtouta", "10:07"), Stop("Baba Ali", "10:11"), Stop("Ain Naadja", "10:15"), Stop("Gué Cne", "10:18"), Stop("El Harrach", "10:22"), Stop("Caroubier", "10:25"), Stop("H.Dey", "10:28"), Stop("Ateliers", "10:32"), Stop("Agha", "10:35"))),
        Train("1512", listOf(Stop("Zéralda", "11:20"), Stop("Université", "11:28"), Stop("Sidi Abdellah", "11:31"), Stop("Tessala El Merdja", "11:37"), Stop("Birtouta", "11:42"), Stop("Baba Ali", "11:46"), Stop("Ain Naadja", "11:50"), Stop("Gué Cne", "11:53"), Stop("El Harrach", "11:57"), Stop("Caroubier", "12:00"), Stop("H.Dey", "12:03"), Stop("Ateliers", "12:07"), Stop("Agha", "12:10"))),
        Train("B518", listOf(Stop("Zéralda", "12:35"), Stop("Université", "12:43"), Stop("Sidi Abdellah", "12:46"), Stop("Tessala El Merdja", "12:52"), Stop("Birtouta", "12:57"), Stop("Baba Ali", "13:01"), Stop("Ain Naadja", "13:05"), Stop("Gué Cne", "13:08"), Stop("El Harrach", "13:12"), Stop("Caroubier", "13:15"), Stop("H.Dey", "13:18"), Stop("Ateliers", "13:22"), Stop("Agha", "13:25"))),
        Train("1514", listOf(Stop("Zéralda", "13:10"), Stop("Université", "13:18"), Stop("Sidi Abdellah", "13:21"), Stop("Tessala El Merdja", "13:27"), Stop("Birtouta", "13:32"), Stop("Baba Ali", "13:36"), Stop("Ain Naadja", "13:40"), Stop("Gué Cne", "13:43"), Stop("El Harrach", "13:47"), Stop("Caroubier", "13:50"), Stop("H.Dey", "13:53"), Stop("Ateliers", "13:57"), Stop("Agha", "14:00"))),
        Train("1518", listOf(Stop("Zéralda", "14:15"), Stop("Université", "14:23"), Stop("Sidi Abdellah", "14:26"), Stop("Tessala El Merdja", "14:32"), Stop("Birtouta", "14:37"), Stop("Baba Ali", "14:41"), Stop("Ain Naadja", "14:45"), Stop("Gué Cne", "14:48"), Stop("El Harrach", "14:52"), Stop("Caroubier", "14:55"), Stop("H.Dey", "14:58"), Stop("Ateliers", "15:02"), Stop("Agha", "15:05"))),
        Train("1520", listOf(Stop("Zéralda", "15:25"), Stop("Université", "15:33"), Stop("Sidi Abdellah", "15:36"), Stop("Tessala El Merdja", "15:42"), Stop("Birtouta", "15:47"), Stop("Baba Ali", "15:51"), Stop("Ain Naadja", "15:55"), Stop("Gué Cne", "15:58"), Stop("El Harrach", "16:02"), Stop("Caroubier", "16:05"), Stop("H.Dey", "16:08"), Stop("Ateliers", "16:12"), Stop("Agha", "16:15"))),
        Train("1522", listOf(Stop("Zéralda", "16:25"), Stop("Université", "16:33"), Stop("Sidi Abdellah", "16:36"), Stop("Tessala El Merdja", "16:42"), Stop("Birtouta", "16:47"), Stop("Baba Ali", "16:51"), Stop("Ain Naadja", "16:55"), Stop("Gué Cne", "16:58"), Stop("El Harrach", "17:02"), Stop("Caroubier", "17:05"), Stop("H.Dey", "17:08"), Stop("Ateliers", "17:12"), Stop("Agha", "17:15"))),
        Train("1524", listOf(Stop("Zéralda", "17:30"), Stop("Université", "17:38"), Stop("Sidi Abdellah", "17:41"), Stop("Tessala El Merdja", "17:47"), Stop("Birtouta", "17:52"), Stop("Baba Ali", "17:56"), Stop("Ain Naadja", "18:00"), Stop("Gué Cne", "18:03"), Stop("El Harrach", "18:07"), Stop("Caroubier", "18:10"), Stop("H.Dey", "18:13"), Stop("Ateliers", "18:17"), Stop("Agha", "18:20"))),
        Train("1526", listOf(Stop("Zéralda", "18:35"), Stop("Université", "18:43"), Stop("Sidi Abdellah", "18:46"), Stop("Tessala El Merdja", "18:52"), Stop("Birtouta", "18:57"), Stop("Baba Ali", "19:01"), Stop("Ain Naadja", "19:05"), Stop("Gué Cne", "19:08"), Stop("El Harrach", "19:12"), Stop("Caroubier", "19:15"), Stop("H.Dey", "19:18"), Stop("Ateliers", "19:22"), Stop("Agha", "19:25"))),

        // --- AGHA -> AÉROPORT HOUARI BOUMEDIENE ---
        Train("B609", listOf(Stop("Agha", "04:40"), Stop("El Harrach", "04:46"), Stop("Bab Ezzouar", "04:56"), Stop("Aéroport", "05:50"))),
        Train("B611", listOf(Stop("Agha", "06:00"), Stop("El Harrach", "06:09"), Stop("Bab Ezzouar", "06:16"), Stop("Aéroport", "06:20"))),
        Train("B613", listOf(Stop("Agha", "07:20"), Stop("El Harrach", "07:29"), Stop("Bab Ezzouar", "07:36"), Stop("Aéroport", "07:40"))),
        Train("B615", listOf(Stop("Agha", "08:35"), Stop("El Harrach", "08:44"), Stop("Bab Ezzouar", "08:51"), Stop("Aéroport", "08:55"))),
        Train("B617", listOf(Stop("Agha", "09:55"), Stop("El Harrach", "10:04"), Stop("Bab Ezzouar", "10:11"), Stop("Aéroport", "10:15"))),
        Train("B619", listOf(Stop("Agha", "11:20"), Stop("El Harrach", "11:29"), Stop("Bab Ezzouar", "11:36"), Stop("Aéroport", "11:40"))),
        Train("B621", listOf(Stop("Agha", "12:40"), Stop("El Harrach", "12:49"), Stop("Bab Ezzouar", "12:56"), Stop("Aéroport", "13:00"))),
        Train("B623", listOf(Stop("Agha", "14:00"), Stop("El Harrach", "14:09"), Stop("Bab Ezzouar", "14:16"), Stop("Aéroport", "14:20"))),
        Train("B625", listOf(Stop("Agha", "15:25"), Stop("El Harrach", "15:34"), Stop("Bab Ezzouar", "15:41"), Stop("Aéroport", "15:45"))),
        Train("B627", listOf(Stop("Agha", "16:45"), Stop("El Harrach", "16:54"), Stop("Bab Ezzouar", "17:01"), Stop("Aéroport", "17:05"))),
        Train("B629", listOf(Stop("Agha", "18:10"), Stop("El Harrach", "18:19"), Stop("Bab Ezzouar", "18:26"), Stop("Aéroport", "18:30"))),
        Train("B631", listOf(Stop("Agha", "19:30"), Stop("El Harrach", "19:39"), Stop("Bab Ezzouar", "19:46"), Stop("Aéroport", "19:50"))),
        Train("B633", listOf(Stop("Agha", "20:40"), Stop("El Harrach", "20:49"), Stop("Bab Ezzouar", "20:56"), Stop("Aéroport", "21:00"))),
        Train("B635", listOf(Stop("Agha", "21:50"), Stop("El Harrach", "21:59"), Stop("Bab Ezzouar", "22:06"), Stop("Aéroport", "22:10"))),

        // --- AÉROPORT HOUARI BOUMEDIENE -> AGHA ---
        Train("B610", listOf(Stop("Aéroport", "05:20"), Stop("Bab Ezzouar", "05:24"), Stop("El Harrach", "05:31"), Stop("Agha", "05:40"))),
        Train("B612", listOf(Stop("Aéroport", "06:35"), Stop("Bab Ezzouar", "06:39"), Stop("El Harrach", "06:46"), Stop("Agha", "06:55"))),
        Train("B614", listOf(Stop("Aéroport", "08:05"), Stop("Bab Ezzouar", "08:09"), Stop("El Harrach", "08:16"), Stop("Agha", "08:25"))),
        Train("B616", listOf(Stop("Aéroport", "09:15"), Stop("Bab Ezzouar", "09:19"), Stop("El Harrach", "09:26"), Stop("Agha", "09:35"))),
        Train("B618", listOf(Stop("Aéroport", "10:30"), Stop("Bab Ezzouar", "10:34"), Stop("El Harrach", "10:41"), Stop("Agha", "10:50"))),
        Train("B620", listOf(Stop("Aéroport", "12:00"), Stop("Bab Ezzouar", "12:04"), Stop("El Harrach", "12:11"), Stop("Agha", "12:20"))),
        Train("B622", listOf(Stop("Aéroport", "13:20"), Stop("Bab Ezzouar", "13:24"), Stop("El Harrach", "13:31"), Stop("Agha", "13:40"))),
        Train("B624", listOf(Stop("Aéroport", "14:45"), Stop("Bab Ezzouar", "14:49"), Stop("El Harrach", "14:56"), Stop("Agha", "15:05"))),
        Train("B626", listOf(Stop("Aéroport", "16:10"), Stop("Bab Ezzouar", "16:14"), Stop("El Harrach", "16:21"), Stop("Agha", "16:30"))),
        Train("B628", listOf(Stop("Aéroport", "17:25"), Stop("Bab Ezzouar", "17:29"), Stop("El Harrach", "17:36"), Stop("Agha", "17:45"))),
        Train("B630", listOf(Stop("Aéroport", "18:55"), Stop("Bab Ezzouar", "18:59"), Stop("El Harrach", "19:06"), Stop("Agha", "19:15"))),
        Train("B632", listOf(Stop("Aéroport", "20:05"), Stop("Bab Ezzouar", "20:09"), Stop("El Harrach", "20:16"), Stop("Agha", "20:25"))),
        Train("B634", listOf(Stop("Aéroport", "21:15"), Stop("Bab Ezzouar", "21:19"), Stop("El Harrach", "21:26"), Stop("Agha", "21:35"))),
        Train("B636", listOf(Stop("Aéroport", "22:25"), Stop("Bab Ezzouar", "22:29"), Stop("El Harrach", "22:36"), Stop("Agha", "22:45"))),

        // --- ALGER -> THENIA -> OUED AÏSSI (V)---
        Train("105", listOf(Stop("Agha", "06:45"), Stop("El Harrach", "06:52"), Stop("Dar El Beida", "07:01"), Stop("Rouiba", "07:05"), Stop("Reghaia", "07:10"), Stop("Boumerdes", "07:21"), Stop("Thenia", "07:29"), Stop("Si Mustapha", "07:36"), Stop("Bordj Menaiel", "07:45"), Stop("Naciria", "07:52"), Stop("Tadmait", "07:58"), Stop("Draâ Ben Khedda", "08:03"), Stop("Boukhalfa", "08:07"), Stop("Tizi Ouzou", "08:11"), Stop("Kef Naâdja", "08:15"), Stop("Oued Aïssi U.", "08:20"), Stop("Oued Aïssi", "08:23"))),
        Train("119", listOf(Stop("Agha", "16:24"), Stop("El Harrach", "16:31"), Stop("Dar El Beida", "16:40"), Stop("Rouiba", "16:44"), Stop("Reghaia", "16:49"), Stop("Boumerdes", "17:00"), Stop("Thenia", "17:09"), Stop("Si Mustapha", "17:16"), Stop("Bordj Menaiel", "17:25"), Stop("Naciria", "17:32"), Stop("Tadmait", "17:38"), Stop("Draâ Ben Khedda", "17:43"), Stop("Boukhalfa", "17:47"), Stop("Tizi Ouzou", "17:51"), Stop("Kef Naâdja", "17:55"), Stop("Oued Aïssi U.", "18:00"), Stop("Oued Aïssi", "18:03"))),
        Train("121", listOf(Stop("Agha", "17:35"), Stop("El Harrach", "17:44"), Stop("Dar El Beida", "17:53"), Stop("Rouiba", "17:57"), Stop("Reghaia", "18:02"), Stop("Boumerdes", "18:13"), Stop("Thenia", "18:21"), Stop("Si Mustapha", "18:26"), Stop("Bordj Menaiel", "18:35"), Stop("Naciria", "18:42"), Stop("Tadmait", "18:48"), Stop("Draâ Ben Khedda", "18:53"), Stop("Boukhalfa", "18:57"), Stop("Tizi Ouzou", "19:01"), Stop("Kef Naâdja", "19:05"), Stop("Oued Aïssi U.", "19:10"), Stop("Oued Aïssi", "19:13"))),
        Train("B133", listOf(Stop("Thenia", "05:15"), Stop("Si Mustapha", "05:19"), Stop("Bordj Menaiel", "05:26"), Stop("Naciria", "05:33"), Stop("Tadmait", "05:38"), Stop("Draâ Ben Khedda", "05:42"), Stop("Boukhalfa", "05:47"), Stop("Tizi Ouzou", "05:50"), Stop("Kef Naâdja", "06:03"), Stop("Oued Aïssi U.", "06:06"), Stop("Oued Aïssi", "06:09"))),
        Train("B135", listOf(Stop("Thenia", "06:25"), Stop("Si Mustapha", "06:29"), Stop("Bordj Menaiel", "06:39"), Stop("Naciria", "06:46"), Stop("Tadmait", "06:52"), Stop("Draâ Ben Khedda", "06:57"), Stop("Boukhalfa", "07:03"), Stop("Tizi Ouzou", "07:07"), Stop("Kef Naâdja", "07:11"), Stop("Oued Aïssi U.", "07:16"), Stop("Oued Aïssi", "07:19"))),
        Train("B137", listOf(Stop("Thenia", "07:32"), Stop("Si Mustapha", "07:36"), Stop("Bordj Menaiel", "07:45"), Stop("Naciria", "07:52"), Stop("Tadmait", "07:58"), Stop("Draâ Ben Khedda", "08:03"), Stop("Boukhalfa", "08:07"), Stop("Tizi Ouzou", "08:11"), Stop("Kef Naâdja", "08:15"), Stop("Oued Aïssi U.", "08:20"), Stop("Oued Aïssi", "08:23"))),
        Train("B139", listOf(Stop("Thenia", "09:20"), Stop("Si Mustapha", "09:24"), Stop("Bordj Menaiel", "09:33"), Stop("Naciria", "09:40"), Stop("Tadmait", "09:46"), Stop("Draâ Ben Khedda", "09:53"), Stop("Boukhalfa", "09:56"), Stop("Tizi Ouzou", "10:00"), Stop("Kef Naâdja", "10:04"), Stop("Oued Aïssi U.", "10:09"), Stop("Oued Aïssi", "10:12"))),
        Train("B141", listOf(Stop("Thenia", "11:05"), Stop("Si Mustapha", "11:09"), Stop("Bordj Menaiel", "11:18"), Stop("Naciria", "11:30"), Stop("Tadmait", "11:36"), Stop("Draâ Ben Khedda", "11:41"), Stop("Boukhalfa", "11:45"), Stop("Tizi Ouzou", "11:49"), Stop("Kef Naâdja", "11:53"), Stop("Oued Aïssi U.", "11:58"), Stop("Oued Aïssi", "12:01"))),
        Train("B143", listOf(Stop("Thenia", "12:05"), Stop("Si Mustapha", "12:09"), Stop("Bordj Menaiel", "12:28"), Stop("Naciria", "12:25"), Stop("Tadmait", "12:31"), Stop("Draâ Ben Khedda", "12:36"), Stop("Boukhalfa", "12:44"), Stop("Tizi Ouzou", "12:48"), Stop("Kef Naâdja", "12:52"), Stop("Oued Aïssi U.", "12:57"), Stop("Oued Aïssi", "13:00"))),
        Train("B145", listOf(Stop("Thenia", "13:30"), Stop("Si Mustapha", "13:34"), Stop("Bordj Menaiel", "13:43"), Stop("Naciria", "13:50"), Stop("Tadmait", "13:56"), Stop("Draâ Ben Khedda", "14:01"), Stop("Boukhalfa", "14:05"), Stop("Tizi Ouzou", "14:09"), Stop("Kef Naâdja", "14:13"), Stop("Oued Aïssi U.", "14:18"), Stop("Oued Aïssi", "14:21"))),
        Train("103", listOf(Stop("Alger", "05:45"), Stop("Agha", "05:48"), Stop("Ateliers", "05:51"), Stop("H.Dey", "05:55"), Stop("Caroubier", "05:58"), Stop("El Harrach", "06:01"), Stop("Oued Smar", "06:06"), Stop("Bab Ezzouar", "06:09"), Stop("Dar El Beida", "06:12"), Stop("Rouiba", "06:17"), Stop("Reghaia", "06:22"), Stop("Boudouaou", "06:28"), Stop("Corso", "06:31"), Stop("Boumerdes", "06:35"), Stop("Tidjelabine", "06:39"), Stop("Thenia", "06:44"))),

        // --- Oued Aïssi -> THENIA -> ALGER (V) ---
        Train("B114", listOf(Stop("Oued Aïssi", "05:30"), Stop("Oued Aïssi U.", "05:32"), Stop("Kef Naâdja", "05:37"), Stop("Tizi Ouzou", "05:41"), Stop("Boukhalfa", "05:45"), Stop("Draâ Ben Khedda", "05:49"), Stop("Tadmait", "05:54"), Stop("Naciria", "06:00"), Stop("Bordj Menaiel", "06:13"), Stop("Si Mustapha", "06:17"), Stop("Thenia", "06:21"), Stop("Boumerdes", "06:29"), Stop("Reghaia", "06:39"), Stop("Rouiba", "06:45"), Stop("Dar El Beida", "06:51"), Stop("El Harrach", "07:00"), Stop("Agha", "07:10"))),
        Train("104", listOf(Stop("Oued Aïssi", "06:40"), Stop("Oued Aïssi U.", "06:42"), Stop("Kef Naâdja", "06:47"), Stop("Tizi Ouzou", "06:51"), Stop("Boukhalfa", "06:55"), Stop("Draâ Ben Khedda", "06:58"), Stop("Tadmait", "07:03"), Stop("Naciria", "07:09"), Stop("Bordj Menaiel", "07:21"), Stop("Si Mustapha", "07:25"), Stop("Thenia", "07:30"), Stop("Boumerdes", "07:40"), Stop("Reghaia", "07:50"), Stop("Rouiba", "07:56"), Stop("Dar El Beida", "08:02"), Stop("El Harrach", "08:10"), Stop("Agha", "08:20"))),
        Train("B134", listOf(Stop("Oued Aïssi", "07:40"), Stop("Oued Aïssi U.", "07:42"), Stop("Kef Naâdja", "07:47"), Stop("Tizi Ouzou", "07:51"), Stop("Boukhalfa", "07:55"), Stop("Draâ Ben Khedda", "07:59"), Stop("Tadmait", "08:09"), Stop("Naciria", "08:15"), Stop("Bordj Menaiel", "08:28"), Stop("Si Mustapha", "08:32"), Stop("Thenia", "08:37"))),
        Train("B136", listOf(Stop("Oued Aïssi", "09:30"), Stop("Oued Aïssi U.", "09:32"), Stop("Kef Naâdja", "09:37"), Stop("Tizi Ouzou", "09:41"), Stop("Boukhalfa", "09:45"), Stop("Draâ Ben Khedda", "09:49"), Stop("Tadmait", "09:57"), Stop("Naciria", "10:03"), Stop("Bordj Menaiel", "10:14"), Stop("Si Mustapha", "10:18"), Stop("Thenia", "10:23"))),
        Train("B138", listOf(Stop("Oued Aïssi", "10:45"), Stop("Oued Aïssi U.", "10:47"), Stop("Kef Naâdja", "10:52"), Stop("Tizi Ouzou", "10:56"), Stop("Boukhalfa", "11:00"), Stop("Draâ Ben Khedda", "11:04"), Stop("Tadmait", "11:09"), Stop("Naciria", "11:15"), Stop("Bordj Menaiel", "11:28"), Stop("Si Mustapha", "11:32"), Stop("Thenia", "11:37"))),
        Train("B140", listOf(Stop("Oued Aïssi", "12:20"), Stop("Oued Aïssi U.", "12:22"), Stop("Kef Naâdja", "12:27"), Stop("Tizi Ouzou", "12:31"), Stop("Boukhalfa", "12:35"), Stop("Draâ Ben Khedda", "12:39"), Stop("Tadmait", "12:44"), Stop("Naciria", "12:50"), Stop("Bordj Menaiel", "13:03"), Stop("Si Mustapha", "13:07"), Stop("Thenia", "13:12"))),
        Train("B142", listOf(Stop("Oued Aïssi", "13:35"), Stop("Oued Aïssi U.", "13:37"), Stop("Kef Naâdja", "13:42"), Stop("Tizi Ouzou", "13:46"), Stop("Boukhalfa", "13:50"), Stop("Draâ Ben Khedda", "13:54"), Stop("Tadmait", "14:07"), Stop("Naciria", "14:13"), Stop("Bordj Menaiel", "14:26"), Stop("Si Mustapha", "14:30"), Stop("Thenia", "14:35"))),
        Train("B144", listOf(Stop("Oued Aïssi", "14:40"), Stop("Oued Aïssi U.", "14:42"), Stop("Kef Naâdja", "14:47"), Stop("Tizi Ouzou", "14:51"), Stop("Boukhalfa", "14:55"), Stop("Draâ Ben Khedda", "14:59"), Stop("Tadmait", "15:04"), Stop("Naciria", "15:10"), Stop("Bordj Menaiel", "15:25"), Stop("Si Mustapha", "15:29"), Stop("Thenia", "15:34"))),
        Train("118", listOf(Stop("Oued Aïssi", "16:20"), Stop("Oued Aïssi U.", "16:22"), Stop("Kef Naâdja", "16:27"), Stop("Tizi Ouzou", "16:31"), Stop("Boukhalfa", "16:35"), Stop("Draâ Ben Khedda", "16:39"), Stop("Tadmait", "16:44"), Stop("Naciria", "16:50"), Stop("Bordj Menaiel", "17:02"), Stop("Si Mustapha", "17:06"), Stop("Thenia", "17:11"), Stop("Boumerdes", "17:19"), Stop("Reghaia", "17:29"), Stop("Rouiba", "17:35"), Stop("Dar El Beida", "17:39"), Stop("El Harrach", "17:48"), Stop("Agha", "17:58"))),
        Train("B146", listOf(Stop("Oued Aïssi", "17:35"), Stop("Oued Aïssi U.", "17:37"), Stop("Kef Naâdja", "17:42"), Stop("Tizi Ouzou", "17:46"), Stop("Boukhalfa", "17:56"), Stop("Draâ Ben Khedda", "18:00"), Stop("Tadmait", "18:05"), Stop("Naciria", "18:10"), Stop("Bordj Menaiel", "18:21"), Stop("Si Mustapha", "18:25"), Stop("Thenia", "18:32"))),
        Train("120", listOf(Stop("Oued Aïssi", "18:30"), Stop("Oued Aïssi U.", "18:32"), Stop("Kef Naâdja", "18:37"), Stop("Tizi Ouzou", "18:41"), Stop("Boukhalfa", "18:45"), Stop("Draâ Ben Khedda", "18:49"), Stop("Tadmait", "18:59"), Stop("Naciria", "19:05"), Stop("Bordj Menaiel", "19:17"), Stop("Si Mustapha", "19:21"), Stop("Thenia", "19:26"))),

        // --- THENIA -> ZÉRALDA ---
        Train("B102/507", listOf(Stop("Thenia", "10:20"), Stop("Tidjelabine", "10:25"), Stop("Boumerdes", "10:30"), Stop("Corso", "10:35"), Stop("Boudouaou", "10:40"), Stop("Reghaia", "10:44"), Stop("Reghaia Ind", "10:47"), Stop("Rouiba Ind", "10:48"), Stop("Rouiba", "10:50"), Stop("Dar El Beida", "10:54"), Stop("Bab Ezzouar", "10:57"), Stop("Oued Smar", "11:01"), Stop("Gué de Cne", "11:10"), Stop("Ain Naadja", "11:14"), Stop("Baba Ali", "11:18"), Stop("Birtouta", "11:23"), Stop("Tessala El Merdja", "11:31"), Stop("Sidi Abde allah", "11:36"), Stop("Sidi Abde allah-U", "11:39"), Stop("Zéralda", "11:43"))),

        // --- ZÉRALDA -> THENIA ---
        Train("B506/101", listOf(Stop("Zéralda", "08:00"), Stop("Sidi Abdel allah-U", "08:03"), Stop("Sidi Abdel allah", "08:06"), Stop("Tessala El Merdja", "08:12"), Stop("Birtouta", "08:19"), Stop("Baba Ali", "08:24"), Stop("Ain Naadja", "08:28"), Stop("Gué Cne", "08:31"), Stop("Oued Smar", "08:42"), Stop("Bab Ezzouar", "08:46"), Stop("Dar El Beida", "08:49"), Stop("Rouiba", "08:55"), Stop("Rouiba Ind", "08:58"), Stop("Reghaia Ind", "08:59"), Stop("Reghaia", "09:01"), Stop("Boudouaou", "09:07"), Stop("Corso", "09:10"), Stop("Boumerdes", "09:14"), Stop("Tidjelabine", "09:18"), Stop("Thenia", "09:23"))),

        // --- GRANDES LIGNES --- آغا – الشلف – وهران - تلمسان
        Train("1081", listOf(Stop("Khemis", "05:45"), Stop("Sidi Lakhdar", "05:51"), Stop("Arib", "06:01"), Stop("Ain Defla", "06:10"), Stop("El Amra", "06:21"), Stop("Rouina", "06:28"), Stop("Cheikh Ben Yahia", "06:32"), Stop("Sidi Bouabida", "06:38"), Stop("Al Attaf", "06:43"), Stop("Bir Saf Saf", "07:02"), Stop("Oued Fodda", "07:09"), Stop("Le Barrage", "07:18"), Stop("Moudrou", "07:25"), Stop("Chlef", "07:32"))),
        Train("1001", listOf(Stop("Agha", "05:50"), Stop("El Harrach", "06:01"), Stop("Blida", "06:42"), Stop("El Affroun", "07:05"), Stop("Khemis", "08:00"), Stop("Arib", "08:16"), Stop("Ain Defla", "08:26"), Stop("Rouina", "08:40"), Stop("Al Attaf", "08:53"), Stop("Oued Fodda", "09:06"), Stop("Chlef", "09:24"), Stop("Boukadir", "09:53"), Stop("Oued Rhiou", "10:09"), Stop("Relizane", "10:36"), Stop("Mohammadia", "11:09"), Stop("Sig", "11:28"), Stop("Oued Tlelat", "11:45"), Stop("Oran", "12:05"))),
        Train("AM", listOf(Stop("Agha", "08:00"), Stop("El Harrach", "12:38"), Stop("Chlef", "10:38"), Stop("Oran", "12:38"))),
        Train("B19", listOf(Stop("Agha", "09:45"), Stop("Ain Defla", "10:30"), Stop("Chlef", "12:33"), Stop("Relizane", "13:28"), Stop("Mohammadia", "13:58"), Stop("Oued Tlelat", "14:27"), Stop("Oran", "14:47"))),
        Train("B11", listOf(Stop("Agha", "12:30"), Stop("El Harrach", "12:38"), Stop("Blida", "13:06"), Stop("El Affroun", "13:19"), Stop("Khemis", "14:08"), Stop("Arib", "14:19"), Stop("Ain Defla", "14:27"), Stop("Rouina", "14:38"), Stop("Al Attaf", "14:48"), Stop("Oued Fodda", "14:57"), Stop("Chlef", "15:12"), Stop("Boukadir", "15:31"), Stop("Oued Rhiou", "15:47"), Stop("Relizane", "16:25"), Stop("Mohammadia", "16:58"), Stop("Sig", "17:17"), Stop("Oued Tlelat", "17:34"), Stop("Oran", "17:54"))),
        Train("1005", listOf(Stop("Agha", "17:00"), Stop("Ain Defla", "18:32"), Stop("Chlef", "19:05"), Stop("Relizane", "19:53"), Stop("Mohammadia", "20:20"), Stop("Oran", "21:01"))),
        Train("1085", listOf(Stop("Agha", "17:23"), Stop("El Harrach", "17:36"), Stop("Blida", "18:15"), Stop("El Affroun", "18:34"), Stop("Boumedfaa", "19:12"), Stop("Khemis", "19:42"), Stop("Arib", "19:54"), Stop("Ain Defla", "20:02"), Stop("El Amra", "20:12"), Stop("Rouina", "20:18"), Stop("Cheikh Ben Yahia", "20:22"), Stop("Sidi Bouabida", "20:28"), Stop("Al Attaf", "20:33"), Stop("Oued Fodda", "20:44"), Stop("Chlef", "21:01"))),
        Train("B13", listOf(Stop("Agha", "22:00"), Stop("Blida", "22:50"), Stop("Ain Defla", "00:15"), Stop("Chlef", "01:04"), Stop("Relizane", "02:07"), Stop("Mohammadia", "02:41"), Stop("Oran", "03:30"), Stop("Sidi Belabas", "05:22"))),


        // --- GRANDES LIGNES --- تلمسان – وهران –- الشلف - آغا
        Train("1084", listOf(Stop("Chlef", "05:00"), Stop("Oued Fodda", "05:16"), Stop("Al Attaf", "05:26"), Stop("Sidi Bouabida", "05:31"), Stop("Cheikh Ben Yahia", "05:37"), Stop("Rouina", "05:41"), Stop("El Amra", "05:47"), Stop("Ain Defla", "05:56"), Stop("Arib", "06:05"), Stop("Khemis", "06:16"), Stop("Boumedfaa", "06:45"), Stop("El Affroun", "07:06"), Stop("Blida", "07:23"), Stop("El Harrach", "07:59"), Stop("Agha", "08:12"))),
        Train("1002", listOf(Stop("Oran", "06:00"), Stop("Oued Tlelat", "06:17"), Stop("Sig", "06:34"), Stop("Mohammadia", "06:52"), Stop("Relizane", "07:25"), Stop("Oued Rhiou", "07:53"), Stop("Boukadir", "08:09"), Stop("Chlef", "08:26"), Stop("Oued Fodda", "08:47"), Stop("Al Attaf", "08:58"), Stop("Rouina", "09:10"), Stop("Ain Defla", "09:22"), Stop("Arib", "09:32"), Stop("Khemis", "09:44"), Stop("El Affroun", "10:35"), Stop("Blida", "10:53"), Stop("El Harrach", "11:31"), Stop("Agha", "11:45"))),
        Train("MA", listOf(Stop("Oran", "08:00"), Stop("Chlef", "09:56"), Stop("Agha", "12:38"))),
        Train("OA", listOf(Stop("Oran", "10:00"), Stop("Mohammadia", "10:42"), Stop("Relizane", "11:11"), Stop("Chlef", "12:06"), Stop("Ain Defla", "12:51"), Stop("Blida", "14:09"), Stop("Agha", "14:56"))),
        Train("B12", listOf(Stop("Oran", "12:30"), Stop("Oued Tlelat", "12:47"), Stop("Sig", "13:05"), Stop("Mohammadia", "13:22"), Stop("Relizane", "13:56"), Stop("Oued Rhiou", "14:23"), Stop("Boukadir", "14:38"), Stop("Chlef", "14:53"), Stop("Oued Fodda", "15:16"), Stop("Al Attaf", "15:26"), Stop("Cheikh Ben Yahia", "15:36"), Stop("El Amra", "15:49"), Stop("Ain Defla", "16:00"), Stop("Sidi Lakhdar", "16:10"), Stop("Boumedfaa", "17:03"), Stop("El Affroun", "17:16"), Stop("Birtouta", "17:43"), Stop("El Harrach", "17:54"), Stop("Agha", "18:00"))),
        Train("1006", listOf(Stop("Oran", "17:00"), Stop("Mohammadia", "17:42"), Stop("Relizane", "18:08"), Stop("Chlef", "18:52"), Stop("Ain Defla", "19:29"), Stop("Blida", "20:28"), Stop("Agha", "21:01"))),
        Train("1088", listOf(Stop("Chlef", "17:10"), Stop("Moudrou", "17:16"), Stop("Le Barrage", "17:23"), Stop("Oued Fodda", "17:32"), Stop("Bir Saf Saf", "17:38"), Stop("Al Attaf", "17:45"), Stop("Sidi Bouabida", "17:50"), Stop("Cheikh Ben Yahia", "17:56"), Stop("Rouina", "18:00"), Stop("El Amra", "18:07"), Stop("Ain Defla", "18:17"), Stop("Arib", "18:26"), Stop("Sidi Lakhdar", "18:36"), Stop("Khemis", "18:43"))),
        Train("B18", listOf(Stop("Telemcen", "19:30"), Stop("Sidi Belabas", "20:48"), Stop("Oued Tlelat", "21:34"), Stop("Oran", "22:04"), Stop("Mohammadia", "23:44"), Stop("Relizane", "00:20"), Stop("Oued Rhiou", "01:30"), Stop("Chlef", "01:30"), Stop("Ain Defla", "02:29"), Stop("Blida", "04:02"), Stop("Agha", "05:00"))),

        // --- GRANDES LIGNES --- آغا – البويرة - قسنطينة – عنابة – بجاية – تقرت – التبسة – باتنة
        Train("B1", listOf(Stop("Agha", "18:47"), Stop("Thenia", "19:35"), Stop("Bouira", "20:51"), Stop("Beni Mansour", "21:38"), Stop("Mansoura", "22:18"), Stop("Bordj Bou Arreridj", "22:46"), Stop("Setif", "23:37"), Stop("El Eulma", "00:06"), Stop("Tadjenanet", "00:29"), Stop("Teleghma", "01:02"), Stop("El Ghourzi", "01:26"), Stop("El Khroub", "02:00"), Stop("Constantine", "02:28"), Stop("El Arrouche", "03:44"), Stop("Ramdane Djamel", "04:00"), Stop("Azzaba", "04:25"), Stop("Annaba", "05:28"))),
        Train("B3", listOf(Stop("Agha", "06:30"), Stop("Boumerdes", "07:05"), Stop("Thenia", "07:14"), Stop("Bouira", "08:26"), Stop("Beni Mansour", "09:14"), Stop("Bordj Bou Arreridj", "10:18"), Stop("Setif", "11:05"), Stop("El Eulma", "11:29"), Stop("Tadjenanet", "11:52"), Stop("Chelghoum Laid", "12:03"), Stop("Teleghma", "12:20"), Stop("El Khroub", "12:54"), Stop("Constantine", "13:14"))),
        Train("13", listOf(Stop("Agha", "14:33"), Stop("Boumerdes", "15:03"), Stop("Thenia", "15:12"), Stop("Lakhdaria", "15:30"), Stop("Bouira", "16:14"), Stop("Hanif", "16:45"), Stop("Beni Mansour", "16:59"), Stop("Tazmalt", "17:13"), Stop("Allaghan", "17:20"), Stop("Akbou", "17:33"), Stop("Ighzer Amokrane", "17:44"), Stop("Takrietz", "17:50"), Stop("Sidi Aich", "17:56"), Stop("El Kseur", "18:13"), Stop("Oued Ghir", "18:26"), Stop("Bejaia", "18:38"))),
        Train("B25/26/27", listOf(Stop("Agha", "18:13"), Stop("Boumerdes", "18:48"), Stop("Thenia", "18:58"), Stop("Bouira", "20:12"), Stop("Beni Mansour", "21:00"), Stop("Mansourah", "21:40"), Stop("Bordj Bou Arreridj", "22:06"), Stop("M'sila", "22:56"), Stop("Barhoum", "23:52"), Stop("Ouled Ammar", "00:14"), Stop("Barika", "00:34"), Stop("Ain Touta", "01:11"), Stop("El Kantara", "01:57"), Stop("El Outaya", "02:27"), Stop("Biskra", "02:56"), Stop("El M'ghair", "04:02"), Stop("Djamaa", "04:29"), Stop("Touggourt", "05:00"))),
        Train("B31", listOf(Stop("Agha", "19:40"), Stop("Boumerdes", "20:27"), Stop("Thenia", "20:37"), Stop("Bouira", "21:53"), Stop("Beni Mansour", "22:40"), Stop("Mansourah", "23:20"), Stop("Bordj Bou Arreridj", "23:48"), Stop("Setif", "00:39"), Stop("El Eulma", "01:08"), Stop("Tadjenanet", "01:30"), Stop("Teleghma", "02:03"), Stop("El Ghourzi", "02:26"), Stop("Ain M'lila", "02:55"), Stop("Ain Fakroun", "03:33"), Stop("Oum El Bouaghi", "04:10"), Stop("Ain Beida", "04:43"), Stop("Sidi Yahia", "06:02"), Stop("Morsott", "06:18"), Stop("Tebessa", "06:51"))),
        Train("7", listOf(Stop("Agha", "13:18"), Stop("Boumerdes", "14:01"), Stop("Bouira", "15:21"), Stop("Beni Mansour", "16:02"), Stop("Bordj Bou Arreridj", "17:04"), Stop("M'sila", "17:48"), Stop("Barhoum", "18:24"), Stop("Ouled Ammar", "18:43"), Stop("Barika", "19:00"), Stop("Ain Touta", "19:38"), Stop("Batna", "20:04"))),
        Train("15", listOf(Stop("Agha", "16:48"), Stop("El Harrach", "16:59"), Stop("Dar El Beida", "17:08"), Stop("Rouiba", "17:14"), Stop("Boumerdes", "17:21"), Stop("Thenia", "17:43"), Stop("Souk El Had", "17:52"), Stop("Beni Amrane", "17:58"), Stop("Ammal", "18:04"), Stop("Lakhdaria", "18:14"), Stop("Kadiria", "18:34"), Stop("Mizan", "18:50"), Stop("Bouira", "19:20"))),


        Train("TA", listOf(Stop("Annaba", "19:20"), Stop("Azzaba", "20:16"), Stop("Ramdane Djamel", "20:41"), Stop("El Arrouche", "20:57"), Stop("Constantine", "21:54"), Stop("El Khroub", "22:18"), Stop("El Ghourzi", "22:34"), Stop("Teleghma", "22:54"), Stop("Tadjenanet", "23:25"), Stop("El Eulma", "23:47"), Stop("Setif", "00:15"), Stop("Bordj Bou Arreridj", "02:07"), Stop("Mansoura", "02:40"), Stop("Beni Mansour", "03:19"), Stop("Bouira", "04:09"), Stop("Thenia", "05:26"), Stop("El Harrach", "06:08"), Stop("Agha", "06:19"))),
        Train("B4", listOf(Stop("Constantine", "06:40"), Stop("El Khroub", "07:07"), Stop("Teleghma", "07:56"), Stop("Chelghoum Laid", "08:13"), Stop("Tadjenanet", "08:24"), Stop("El Eulma", "08:42"), Stop("Setif", "09:08"), Stop("Bordj Bou Arreridj", "09:52"), Stop("Beni Mansour", "11:06"), Stop("Bouira", "11:53"), Stop("Thenia", "13:05"), Stop("Boumerdes", "13:12"), Stop("Agha", "13:52"))),
        Train("14", listOf(Stop("Bejaia", "06:30"), Stop("Oued Ghir", "06:42"), Stop("El Kseur", "06:54"), Stop("Sidi Aich", "07:11"), Stop("Takrietz", "07:19"), Stop("Ighzer Amokrane", "07:25"), Stop("Akbou", "07:35"), Stop("Allaghan", "07:48"), Stop("Tazmalt", "07:54"), Stop("Beni Mansour", "08:03"), Stop("Hanif", "08:15"), Stop("El Adjiba", "08:25"), Stop("Bouira", "08:53"), Stop("Lakhdaria", "09:41"), Stop("Thenia", "10:02"), Stop("Boumerdes", "10:08"), Stop("Agha", "10:39"))),
        Train("B28/29/30", listOf(Stop("Touggourt", "17:30"), Stop("Djamaa", "17:58"), Stop("El M'ghair", "18:25"), Stop("Biskra", "19:25"), Stop("El Outaya", "20:00"), Stop("El Kantara", "20:29"), Stop("Ain Touta", "21:03"), Stop("Barika", "21:50"), Stop("Ouled Ammar", "22:12"), Stop("Barhoum", "22:34"), Stop("M'sila", "23:12"), Stop("Bordj Bou Arreridj", "00:22"), Stop("Mansoura", "01:04"), Stop("Beni Mansour", "01:42"), Stop("Bouira", "02:34"), Stop("Thenia", "03:57"), Stop("Boumerdes", "04:08"), Stop("Agha", "04:53"))),
        Train("B32/B30", listOf(Stop("Tebessa", "17:30"), Stop("Morsott", "18:02"), Stop("Sidi Yahia", "18:18"), Stop("Ain Beida", "19:33"), Stop("Oum El Bouaghi", "20:07"), Stop("Ain Fakroun", "20:45"), Stop("Ain M'lila", "21:23"), Stop("El Ghourzi", "21:35"), Stop("Teleghma", "22:18"), Stop("Tadjenanet", "22:49"), Stop("El Eulma", "23:11"), Stop("Setif", "23:39"), Stop("Bordj Bou Arreridj", "00:28"), Stop("Mansoura", "01:07"), Stop("Beni Mansour", "01:45"), Stop("Bouira", "02:37"), Stop("Thenia", "03:57"), Stop("Boumerdes", "04:08"), Stop("Agha", "04:51"))),
        Train("B6", listOf(Stop("Batna", "22:50"), Stop("Ain Touta", "23:13"), Stop("Barika", "23:51"), Stop("Ouled Ammar", "00:09"), Stop("Barhoum", "00:28"), Stop("M'sila", "01:00"), Stop("Bordj Bou Arreridj", "01:45"), Stop("Beni Mansour", "02:48"), Stop("Bouira", "03:28"), Stop("Boumerdes", "04:43"), Stop("Agha", "05:23"))),
        Train("12", listOf(Stop("Bouira", "05:35"), Stop("Mizan", "06:04"), Stop("Kadiria", "06:17"), Stop("Lakhdaria", "06:30"), Stop("Ammal", "06:40"), Stop("Beni Amrane", "06:44"), Stop("Souk El Had", "06:49"), Stop("Thenia", "06:57"), Stop("Boumerdes", "07:08"), Stop("Rouiba", "07:24"), Stop("Dar El Beida", "07:30"), Stop("El Harrach", "07:38"), Stop("Agha", "07:48"))),

        // --- Bejaia – Beni Mansour - Bejaia

        Train("152", listOf(Stop("Bejaia", "08:00"), Stop("Oued Ghir", "08:13"), Stop("El Kseur", "08:27"), Stop("Il Maten", "08:38"), Stop("Lotta", "08:44"), Stop("Sidi Aich", "08:51"), Stop("Takrietz", "08:59"), Stop("Ighzer Amokrane", "09:07"), Stop("Azib", "09:13"), Stop("Akbou", "09:21"), Stop("Allaghan", "09:36"), Stop("Tazmalt", "09:42"), Stop("Toghza", "10:00"), Stop("Beni Mansour", "10:07"))),
        Train("B160", listOf(Stop("Bejaia", "13:00"), Stop("Oued Ghir", "13:13"), Stop("El Kseur", "13:27"), Stop("Il Maten", "13:38"), Stop("Lotta", "13:44"), Stop("Sidi Aich", "13:50"))),
        Train("B162", listOf(Stop("Bejaia", "16:15"), Stop("Oued Ghir", "16:28"), Stop("El Kseur", "16:43"))),
        Train("154", listOf(Stop("Bejaia", "17:45"), Stop("Oued Ghir", "17:58"), Stop("El Kseur", "18:12"), Stop("Il Maten", "18:23"), Stop("Lotta", "17:44"), Stop("Sidi Aich", "18:29"), Stop("Takrietz", "18:50"), Stop("Ighzer Amokrane", "18:58"), Stop("Akbou", "19:11"), Stop("Allaghan", "19:24"), Stop("Tazmalt", "19:30"), Stop("Toghza", "19:37"), Stop("Beni Mansour", "19:44"))),
        Train("151", listOf(Stop("Beni Mansour", "05:35"), Stop("Toghza", "05:40"), Stop("Tazmalt", "05:44"), Stop("Allaghan", "05:51"), Stop("Akbou", "06:03"), Stop("Azib", "06:11"), Stop("Ighzer Amokrane", "06:16"), Stop("Takrietz", "06:26"), Stop("Sidi Aich", "06:30"), Stop("Lotta", "06:37"), Stop("Il Maten", "06:42"), Stop("El Kseur", "06:51"), Stop("Oued Ghir", "07:09"), Stop("Bejaia", "07:23"))),
        Train("153", listOf(Stop("Beni Mansour", "10:40"), Stop("Toghza", "10:45"), Stop("Tazmalt", "10:50"), Stop("Allaghan", "10:57"), Stop("Akbou", "11:10"), Stop("Azib", "11:18"), Stop("Ighzer Amokrane", "11:23"), Stop("Takrietz", "11:31"), Stop("Sidi Aich", "11:39"), Stop("Lotta", "11:46"), Stop("Il Maten", "11:52"), Stop("El Kseur", "12:01"), Stop("Oued Ghir", "12:15"), Stop("Bejaia", "12:29"))),
        Train("B161", listOf(Stop("Sidi Aich", "14:10"), Stop("Lotta", "14:17"), Stop("Il Maten", "14:23"), Stop("El Kseur", "14:30"), Stop("Oued Ghir", "14:42"), Stop("Bejaia", "15:06"))),
        Train("B163", listOf(Stop("El Kseur", "17:00"), Stop("Oued Ghir", "17:13"), Stop("Bejaia", "17:27"))),


        // --- Tissemsilt/Bughzul/Laghouat lines

        Train("B178", listOf(Stop("Tissemsilt", "05:00"), Stop("Bouguerra", "05:15"), Stop("Hassi Fedoul", "05:35"), Stop("Sidi Ladjal", "05:53"), Stop("Chahbounia", "06:04"), Stop("Boughzoul", "06:35"), Stop("Birine", "06:55"), Stop("Bouti Sayah", "07:24"), Stop("Ain El Hadjel", "07:38"), Stop("M'sila", "08:17"), Stop("M'djaz", "08:38"), Stop("Bordj Bou Arreridj", "09:08"))),
        Train("B179", listOf(Stop("Bordj Bou Arreridj", "16:30"), Stop("M'djaz", "16:58"), Stop("M'sila", "17:18"), Stop("Ain El Hadjel", "17:58"), Stop("Bouti Sayah", "18:13"), Stop("Birine", "18:44"), Stop("Boughzoul", "19:07"), Stop("Chahbounia", "19:43"), Stop("Sidi Ladjal", "19:54"), Stop("Hassi Fedoul", "20:14"), Stop("Bouguerra", "20:33"), Stop("Tissemsilt", "20:48"))),
        Train("B193", listOf(Stop("Birine", "04:50"), Stop("Boughzoul", "05:10"), Stop("Ain Oussera", "05:37"), Stop("Hassi Bahbah", "06:05"), Stop("Djelfa", "06:39"), Stop("Sidi Makhlouf", "07:15"), Stop("Laghouat", "07:45"))),
        Train("B195", listOf(Stop("Djelfa", "11:00"), Stop("Sidi Makhlouf", "11:33"), Stop("Laghouat", "12:03"))),
        Train("B197", listOf(Stop("Djelfa", "14:30"), Stop("Sidi Makhlouf", "15:03"), Stop("Laghouat", "15:33"))),
        Train("B194", listOf(Stop("Laghouat", "09:00"), Stop("Sidi Makhlouf", "09:30"), Stop("Djelfa", "10:03"))),
        Train("B196", listOf(Stop("Laghouat", "12:45"), Stop("Sidi Makhlouf", "13:15"), Stop("Djelfa", "13:48"))),
        Train("B198", listOf(Stop("Laghouat", "16:10"), Stop("Sidi Makhlouf", "16:39"), Stop("Djelfa", "17:13"), Stop("Hassi Bahbah", "17:48"), Stop("Ain Oussera", "18:16"), Stop("Boughzoul", "18:39")))

    )

    // Helper to get the full list for the Reset button
    fun getFullSchedule(): List<Train> = allTrains

    val stations = listOf(
        // --- Algiers Suburbs & Central ---
        "Agha", "Ain Naadja", "Alger", "Ateliers", "Baba Ali", "Bab Ezzouar",
        "Birtouta", "Bordj Menaiel", "Boudouaou", "Boukhalfa", "Boumerdes",
        "Boufarik", "Beni Mered", "Blida", "Caroubier", "Chiffa", "Corso",
        "Dar El Beida", "Draâ Ben Khedda", "El Affroun", "El Harrach",
        "Gué de Cne", "Hussein Dey", "Issers", "Kef Naadja", "Mouzaia",
        "Naceria", "Oued Aissi", "Oued Aissi -Univ.", "Oued Smar", "Reghaia",
        "Reghaia ZI", "Rouiba", "Rouiba ZI", "Sidi AbdAllah", "Sidi AbdAllah-Univ.",
        "Si Mustapha", "Tadmait", "Tessala ElMerdja", "Thenia", "Tidjelabine",
        "Tizi Ouzou", "Zéralda", "Reguieg",

        // --- West Line (Oran / Tlemcen) ---
        "Khemis", "Chlef", "Relizane", "Oran", "Tlemcen", "Sidi Belabes",
        "Sidi Lakhdar", "Arib", "Ain Defla", "El Amra", "Rouina", "Cheikh Ben Yahia",
        "Sidi Bouabida", "El Attaf", "Bir Saf Saf", "Oued Fodda", "Le Barrage",
        "Moudrou", "Boukadir", "Oued Rhiou", "Mohammadia", "Sig", "Oued Tlelat",
        "Boumedfaa", "Hocenia", "Mouzaia",

        // --- East Line (Constantine / Annaba / Bejaia / Tebessa) ---
        "Annaba", "Constantine", "Bejaia", "Tebessa", "Batna", "Bouira",
        "Setif", "Lakhdaria", "M'sila", "El Khroub", "Bordj Bou Arreridj",
        "El Eulma", "Tadjenanet", "Chelghoum Laid", "Teleghma", "El Ghourzi",
        "Ramdane Djamel", "Azzaba", "El Arrouche", "Berrahal", "Mansoura",
        "Beni Mansour", "Hanif", "El Adjiba", "Lakhdaria", "Kadiria", "Mizan",
        "Ammal", "Beni Amrane", "Souk El Had", "Toghza", "Tazmalt", "Allaghan",
        "Akbou", "Ighzer Amokrane", "Takrietz", "Sidi Aich", "Lotta", "Il Maten",
        "El Kseur", "Oued Ghir", "Azib", "Tadjenanet", "Ain M'lila", "Ain Fakroun",
        "Oum El Bouaghi", "Ain Beida", "Sidi Yahia", "Morsott",

        // --- South Line (Biskra / Touggourt / Laghouat) ---
        "Touggourt", "Biskra", "Djelfa", "Laghouat", "M'djaz", "Barhoum",
        "Ouled Ammar", "Barika", "Ain Touta", "El Kantara", "El Outaya",
        "El M'ghair", "Djamaa", "Birine", "Boughzoul", "Ain Oussera",
        "Hassi Bahbah", "Sidi Makhlouf", "Tissemsilt", "Bouguerra",
        "Hassi Fedoul", "Sidi Ladjal", "Chahbounia", "Bouti Sayah", "Ain El Hadjel"
    ).sorted()
    var latestResults: List<Journey> = emptyList()
}

class MainActivity : AppCompatActivity() {

    private var selectedTime: String = "00:00"
    private var trainAdapter: TrainAdapter? = null

    private lateinit var startInput: AutoCompleteTextView
    private lateinit var targetInput: AutoCompleteTextView
    private lateinit var btnFav: Button
    private lateinit var btnSearch: Button
    private lateinit var btnReset: Button
    private lateinit var btnSwap: Button
    private lateinit var ivNoResults: ImageView
    private lateinit var tvHeader: TextView
    private lateinit var btnTime: Button
    private fun saveFavorite(from: String, to: String, time: String) {
        val prefs = getSharedPreferences("DZTrainPrefs", MODE_PRIVATE)
        val gson = Gson()

        val favorites = getFavorites().toMutableList()

        // Check if it already exists
        val exists = favorites.any { it.from == from && it.to == to && it.time == time }

        if (!exists) {
            // Ensure your FavoriteRoute class also accepts 'time'
            favorites.add(FavoriteRoute(from, to, time))

            val json = gson.toJson(favorites)
            prefs.edit().putString("fav_routes", json).apply()

            updateFavoritesUI()
        }
    }

    private fun getFavorites(): List<FavoriteRoute> {
        val prefs = getSharedPreferences("DZTrainPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("fav_routes", null) ?: return emptyList()

        return try {
            val type = object : TypeToken<List<FavoriteRoute>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            // If the data is old/corrupt, return an empty list instead of crashing
            emptyList()
        }
    }
    private fun showAboutDialog() {
        val builder = android.app.AlertDialog.Builder(this)

        // Get CPU architecture (helpful for your Intel i3 TV setup)
        val arch = android.os.Build.SUPPORTED_ABIS.getOrNull(0) ?: "unknown"

        // Colors updated to match timetable and popup logic
        val messageHtml = """
        <b><i><font color='#1976D2'>version ${BuildConfig.VERSION_NAME}</font></i></b><br>
        <small><font color='#757575'>Build: ${BuildConfig.VERSION_CODE} ($arch)</font></small><br><br>
        
        This app provides information about SNTF Suburban Schedule.<br><br>
        ______________________________________<br><br>
        Color code for available lines:<br>
        <font color='#1976D2'>▪ Alger - Thénia </font><br>
        <font color='#CB7DA2'>▪ Alger - El Affroun </font><br>
        <font color='#2E7D32'>▪ Alger - Zéralda </font><br>
        <font color='#FB8C00'>▪ Alger - Bouira </font><br>
        <font color='#7B1FA2'>▪ Alger - Tizi Ouzou / Oued Aissi </font><br>
        ______________________________________<br><br>
        <i><font color='#C2185B'>Created by MAHDAOUI Y.</font></i><br><br>
        <font color='#757575'><small>Built on: ${BuildConfig.BUILD_DATE}</small></font>
    """.trimIndent()

        builder.setTitle("DZTrain")
        builder.setMessage(android.text.Html.fromHtml(messageHtml, android.text.Html.FROM_HTML_MODE_LEGACY))

        builder.setPositiveButton("X") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()

        // Style the button and title
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.parseColor("#1976D2"))
        val titleId = resources.getIdentifier("alertTitle", "id", "android")
        if (titleId > 0) {
            dialog.findViewById<android.widget.TextView>(titleId)?.setTextColor(android.graphics.Color.parseColor("#1976D2"))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

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
        tvHeader = findViewById<TextView>(R.id.tvHeader)
        btnFav = findViewById<Button>(R.id.btnFav)

        trainAdapter = TrainAdapter(
            list = emptyList(),
            onItemClick = { train -> showStopsPopup(this, train) },
            onLongClick = { train -> shareTrainDetails(train) }
        )

        //recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = trainAdapter

        btnFav.setOnClickListener {
            val from = startInput.text.toString().trim()
            val to = targetInput.text.toString().trim()

            if (from.isNotEmpty() && to.isNotEmpty()) {
                // Now passing from, to, AND the selectedTime
                saveFavorite(from, to, selectedTime)

                // Provide visual feedback that it was saved
                Toast.makeText(this, "Saved to favorites!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select both stations", Toast.LENGTH_SHORT).show()
            }
        }

        tvHeader.setOnClickListener {
            showAboutDialog()
        }

        // Initialize RecyclerView LayoutManager once
        //recyclerView.layoutManager = LinearLayoutManager(this)

        // Setup Autocomplete
        val stationAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, TrainRepository.stations)
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
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                selectedTime = String.format("%02d:%02d", hour, minute)
                btnTime.text = "Time: $selectedTime"
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // Search Logic
        btnSearch.setOnClickListener { performSearch()
        }

        btnReset.setOnClickListener {
            startInput.text.clear()
            targetInput.text.clear()

            // Reset clock to "Now"
            val now = java.util.Calendar.getInstance()
            selectedTime = String.format("%02d:%02d",
                now.get(java.util.Calendar.HOUR_OF_DAY),
                now.get(java.util.Calendar.MINUTE)
            )
            btnTime.text = "Departure Time $selectedTime"

            // Reset RecyclerView
            trainAdapter = TrainAdapter(
                list = emptyList(),
                onItemClick = { train -> showStopsPopup(this, train) }, // Parameter 2
                onLongClick = { train -> shareTrainDetails(train) }     // Parameter 3
            )

            // Animation logic
            if (ivNoResults.visibility == View.VISIBLE) {
                ivNoResults.animate().alpha(0f).setDuration(500).withEndAction {
                    ivNoResults.visibility = View.GONE
                }.start()
            }
        }

        // setting menu
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val navView = findViewById<NavigationView>(R.id.navView)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    // Open your Settings Activity/Fragment
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_about -> {
                    // Show a dialog with info about the app
                    showAboutDialog()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        updateFavoritesUI()
    }

    private fun shareTrainDetails(journey: Journey) {
        val message = when (journey) {
            is Journey.Direct -> {
                val train = journey.train
                "Trip Info: ${train.from} -> ${train.to}\n" +
                        "Train: ${train.id}\nDeparture: ${train.depTime}\nArrival: ${train.arrTime}"
            }
            is Journey.Connection -> {
                "Journey Info: ${journey.firstTrain.from} -> ${journey.secondTrain.to} (via ${journey.transferStation})\n" +
                        "Leg 1 (Train ${journey.firstTrain.id}): ${journey.firstTrain.depTime}\n" +
                        "Leg 2 (Train ${journey.secondTrain.id}): ${journey.secondTrain.depTime}\n" +
                        "Total Duration: ${journey.totalDuration}"
            }
        }

        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, message)
        }
        startActivity(android.content.Intent.createChooser(intent, "Share Schedule"))
    }

    private fun performSearch() {
        val startStationView = findViewById<AutoCompleteTextView>(R.id.startStation)
        val targetStationView = findViewById<AutoCompleteTextView>(R.id.targetStation)

        val from = startStationView.text.toString()
        val to = targetStationView.text.toString()

        if (from.isEmpty() || to.isEmpty()) {
            Toast.makeText(this, "Please select both stations", Toast.LENGTH_SHORT).show()
            return
        }

        // ADDED: Pass 'selectedTime' as the third argument here
        val results = findJourneys(from, to, selectedTime)

        if (results.isEmpty()) {
            findViewById<ImageView>(R.id.ivNoResults).visibility = View.VISIBLE
        } else {
            findViewById<ImageView>(R.id.ivNoResults).visibility = View.GONE

            // 1. Save results to the Repository instead of the Intent
            TrainRepository.latestResults = results

            // 2. Open the activity without carrying the heavy data
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }
    }

    data class Train(
        val id: String,
        val stops: List<TrainStop>
    )

    data class TrainStop(
        val name: String,
        val time: String // Expected format "HH:mm" or "-"
    )

    private fun findJourneys(start: String, end: String, time: String, isShortest: Boolean = false): List<Journey> {

        // HELPER 1: Convert "HH:mm" to total minutes
        fun toMinutes(t: String): Int {
            val cleanTime = t.trim()
            if (cleanTime == "-" || !cleanTime.contains(":")) return -1
            return try {
                val parts = cleanTime.split(":")
                parts[0].toInt() * 60 + parts[1].toInt()
            } catch (e: Exception) { -1 }
        }

        // HELPER 2: Convert minutes to "1d 05h 30m" string
        fun formatDuration(totalMins: Int): String {
            val days = totalMins / 1440
            val hours = (totalMins % 1440) / 60
            val mins = totalMins % 60
            return buildString {
                if (days > 0) append("${days}d ")
                if (hours > 0 || days > 0) append("${hours}h ")
                append("${mins}m")
            }.trim()
        }

        // HELPER 3: Calculate rolling time to handle midnight crossings per stop
        fun getAbsoluteTimes(stops: List<Stop>): List<Int> {
            val absoluteMinutes = mutableListOf<Int>()
            var dayOffset = 0
            var lastMins = -1

            for (stop in stops) {
                val currentMins = toMinutes(stop.time)
                if (currentMins == -1) {
                    absoluteMinutes.add(-1)
                    continue
                }
                if (lastMins != -1 && currentMins < (lastMins % 1440)) {
                    dayOffset += 1440
                }
                val absolute = currentMins + dayOffset
                absoluteMinutes.add(absolute)
                lastMins = absolute
            }
            return absoluteMinutes
        }

        // EXTENSION helpers for the Sorting block
        fun Journey.getSortingDuration(): Int = when(this) {
            is Journey.Direct -> this.durationMins
            is Journey.Connection -> this.totalDurationMins
        }

        fun Journey.getSortingArrival(): Int = when(this) {
            is Journey.Direct -> this.arrivalAbsMins
            is Journey.Connection -> this.arrivalAbsMins
        }

        fun formatTime(t: String): String = if (t.length == 4 && t.contains(":")) "0$t" else t
        val safeSelectedTime = formatTime(time)
        val searchTimeMins = toMinutes(safeSelectedTime)

        val finalResults = mutableListOf<Journey>()
        val allTrains = TrainRepository.allTrains ?: return emptyList()

        // 1. Direct Trains Logic
        allTrains.forEach { train ->
            val sIdx = train.stops.indexOfFirst { it.name.equals(start, true) }
            val tIdx = train.stops.indexOfFirst { it.name.equals(end, true) }

            if (sIdx != -1 && tIdx != -1 && tIdx > sIdx) {
                val absTimes = getAbsoluteTimes(train.stops)
                val depAbs = absTimes[sIdx]
                val arrAbs = absTimes[tIdx]

                if (depAbs != -1 && (depAbs % 1440) >= searchTimeMins) {
                    val durationMins = arrAbs - depAbs
                    finalResults.add(Journey.Direct(
                        train = SearchResult(
                            id = train.id, depTime = train.stops[sIdx].time, arrTime = train.stops[tIdx].time,
                            from = train.stops[sIdx].name, to = train.stops[tIdx].name,
                            stops = ArrayList(train.stops.subList(sIdx, tIdx + 1)),
                            isHoliday = isHolidayTrain(train.id),
                            lineName = "${train.stops.first().name} ➔ ${train.stops.last().name}",
                            fullTrainStops = train.stops
                        ),
                        totalDuration = formatDuration(durationMins),
                        durationMins = durationMins,    // Metadata for sorting
                        arrivalAbsMins = arrAbs         // Metadata for sorting
                    ))
                }
            }
        }

        // 2. Connection Logic
        val hubs = listOf("El Harrach", "Birtouta", "Bab Ezzouar", "Oued Smar", "Beni Mansour", "Bordj Bou Arreridj", "Boughzoul", "Thenia", "Constantine")

        for (hub in hubs) {
            if (start.equals(hub, true) || end.equals(hub, true)) continue

            val leg1Trains = allTrains.filter { t ->
                val sIdx = t.stops.indexOfFirst { it.name.equals(start, true) }
                val hIdx = t.stops.indexOfFirst { it.name.equals(hub, true) }
                sIdx != -1 && hIdx != -1 && hIdx > sIdx && toMinutes(t.stops[sIdx].time) >= searchTimeMins
            }

            for (t1 in leg1Trains) {
                val abs1 = getAbsoluteTimes(t1.stops)
                val sIdx1 = t1.stops.indexOfFirst { it.name.equals(start, true) }
                val hIdx1 = t1.stops.indexOfFirst { it.name.equals(hub, true) }
                val arr1Abs = abs1[hIdx1]

                val leg2Trains = allTrains.filter { t2 ->
                    val hIdx = t2.stops.indexOfFirst { it.name.equals(hub, true) }
                    val eIdx = t2.stops.indexOfFirst { it.name.equals(end, true) }
                    if (hIdx == -1 || eIdx == -1 || eIdx <= hIdx) return@filter false
                    true
                }

                for (t2 in leg2Trains) {
                    val abs2 = getAbsoluteTimes(t2.stops)
                    val hIdx2 = t2.stops.indexOfFirst { it.name.equals(hub, true) }
                    val eIdx2 = t2.stops.indexOfFirst { it.name.equals(end, true) }

                    var dep2Abs = abs2[hIdx2]
                    var arr2Abs = abs2[eIdx2]

                    // Align Leg 2 timeline
                    while (dep2Abs <= arr1Abs) {
                        dep2Abs += 1440
                        arr2Abs += 1440
                    }

                    val waitTimeMins = dep2Abs - arr1Abs
                    val totalTimeMins = arr2Abs - abs1[sIdx1]

                    finalResults.add(Journey.Connection(
                        firstTrain = SearchResult(t1.id, t1.stops[sIdx1].time, t1.stops[hIdx1].time, start, hub, ArrayList(t1.stops.subList(sIdx1, hIdx1 + 1)), isHolidayTrain(t1.id), "${t1.stops.first().name} ➔ ${t1.stops.last().name}", t1.stops),
                        secondTrain = SearchResult(t2.id, t2.stops[hIdx2].time, t2.stops[eIdx2].time, hub, end, ArrayList(t2.stops.subList(hIdx2, eIdx2 + 1)), isHolidayTrain(t2.id), "${t2.stops.first().name} ➔ ${t2.stops.last().name}", t2.stops),
                        transferStation = hub,
                        totalDuration = "${formatDuration(totalTimeMins)} (Wait: ${formatDuration(waitTimeMins)})",
                        totalDurationMins = totalTimeMins, // Metadata for sorting
                        arrivalAbsMins = arr2Abs           // Metadata for sorting
                    ))
                }
            }
        }

        // 3. Sorting (Numerical Comparison)
        return finalResults.distinctBy {
            when(it) {
                is Journey.Direct -> "D-${it.train.id}-${it.train.depTime}"
                is Journey.Connection -> "C-${it.firstTrain.id}-${it.secondTrain.id}-${it.firstTrain.depTime}"
            }
        }.sortedWith { j1, j2 ->
            val d1 = j1.getSortingDuration()
            val d2 = j2.getSortingDuration()
            val a1 = j1.getSortingArrival()
            val a2 = j2.getSortingArrival()

            if (isShortest) {
                if (d1 != d2) d1.compareTo(d2) else a1.compareTo(a2)
            } else {
                if (a1 != a2) a1.compareTo(a2) else d1.compareTo(d2)
            }
        }
    }

    private fun updateFavoritesUI() {
        val favList = getFavorites()
        val rvFavorites = findViewById<RecyclerView>(R.id.rvFavorites)
        val tvFavLabel = findViewById<TextView>(R.id.tvFavLabel)

        if (favList.isEmpty()) {
            rvFavorites.visibility = View.GONE
            tvFavLabel.visibility = View.GONE
        } else {
            rvFavorites.visibility = View.VISIBLE
            tvFavLabel.visibility = View.VISIBLE

            // Use standard GridLayoutManager - NO stackFromEnd
            val mLayoutManager = GridLayoutManager(this, 2)
            rvFavorites.layoutManager = mLayoutManager

            val adapter = FavoritesAdapter(favList,
                onClick = { selectedFav ->
                    findViewById<AutoCompleteTextView>(R.id.startStation).setText(selectedFav.from, false)
                    findViewById<AutoCompleteTextView>(R.id.targetStation).setText(selectedFav.to, false)
                    selectedTime = selectedFav.time
                    btnTime.text = "Departure Time: After $selectedTime"
                    performSearch()
                },
                onLongClick = { routeToDelete ->
                    deleteFavorite(routeToDelete)
                }
            )
            rvFavorites.adapter = adapter
        }
    }

    private fun deleteFavorite(route: FavoriteRoute) {
        val prefs = getSharedPreferences("DZTrainPrefs", MODE_PRIVATE)
        val gson = Gson()
        val originalFavorites = getFavorites()
        val updatedFavorites = originalFavorites.toMutableList()

        // 1. Remove the route
        updatedFavorites.removeAll {
            it.from == route.from && it.to == route.to && it.time == route.time
        }

        // 2. Save and Update UI
        val saveList = { list: List<FavoriteRoute> ->
            val json = gson.toJson(list)
            prefs.edit().putString("fav_routes", json).apply()
            updateFavoritesUI()
        }

        saveList(updatedFavorites)

        // 3. Show Snackbar with UNDO instead of just a Toast
        val rootView = findViewById<View>(android.R.id.content)
        com.google.android.material.snackbar.Snackbar.make(rootView, "Favorite removed", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                saveList(originalFavorites) // Put the old list back
            }
            .setActionTextColor(Color.YELLOW)
            .show()
    }

    private fun showStopsPopup(context: Context, journey: Journey) {
        val builder = android.app.AlertDialog.Builder(context)
        var isShowingAll = false

        fun getHeaderColor(trainId: String): String = when {
            trainId == "12" || trainId == "15" -> "#FB8C00"
            trainId.startsWith("15") || trainId.startsWith("B5") -> "#2E7D32"
            trainId.startsWith("B1") || trainId.startsWith("10") -> "#1976D2"
            else -> "#7B1FA2"
        }

        fun formatStops(train: SearchResult, showAll: Boolean): String {
            val stopsToDisplay = if (showAll) train.fullTrainStops else train.stops
            val sb = StringBuilder()
            stopsToDisplay.forEach { stop ->
                val prefix = if (stop.time == "-") " ○ " else " ● "
                val name = stop.name
                val dotPadding = ".".repeat(maxOf(1, 25 - name.length))
                sb.append("$prefix$name $dotPadding ${stop.time}\n")
            }
            return sb.toString()
        }

        // --- NEW: Custom Header Layout ---
        val headerLayout = android.widget.RelativeLayout(context).apply {
            setPadding(50, 40, 50, 40)
        }

        val tvIdLeft = android.widget.TextView(context).apply {
            textSize = 17f
            setTextColor(android.graphics.Color.WHITE)
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            layoutParams = android.widget.RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply { addRule(android.widget.RelativeLayout.ALIGN_PARENT_START) }
        }

        val tvRouteRight = android.widget.TextView(context).apply {
            textSize = 14f
            setTextColor(android.graphics.Color.WHITE)
            layoutParams = android.widget.RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(android.widget.RelativeLayout.ALIGN_PARENT_END)
                addRule(android.widget.RelativeLayout.CENTER_VERTICAL)
            }
        }

        headerLayout.addView(tvIdLeft)
        headerLayout.addView(tvRouteRight)

        val messageView = android.widget.TextView(context).apply {
            textSize = 14f
            setPadding(50, 40, 50, 40)
            setLineSpacing(8f, 1.2f)
            setTextColor(android.graphics.Color.parseColor("#374151"))
            typeface = android.graphics.Typeface.MONOSPACE
        }

        fun updateContent() {
            val finalContent = StringBuilder()
            when (journey) {
                is Journey.Direct -> {
                    val train = journey.train
                    val label = if (isHolidayTrain(train.id)) " (Fri/Hol)" else ""

                    tvIdLeft.text = "Train ${train.id}$label"
                    tvRouteRight.text = train.lineName

                    headerLayout.setBackgroundColor(android.graphics.Color.parseColor(getHeaderColor(train.id)))
                    finalContent.append(formatStops(train, isShowingAll))
                }
                is Journey.Connection -> {
                    tvIdLeft.text = "Connection"
                    tvRouteRight.text = "${journey.firstTrain.from} ➔ ${journey.secondTrain.to}"

                    headerLayout.setBackgroundColor(android.graphics.Color.parseColor("#1F2937"))
                    finalContent.append("LEG 1: ${journey.firstTrain.id}\n")
                    finalContent.append(formatStops(journey.firstTrain, isShowingAll))
                    finalContent.append("\nTRANSFER: ${journey.transferStation}\n")
                    finalContent.append("${journey.totalDuration}\n\n")
                    finalContent.append("LEG 2: ${journey.secondTrain.id}\n")
                    finalContent.append(formatStops(journey.secondTrain, isShowingAll))
                }
            }
            messageView.text = finalContent.toString()
        }

        updateContent()
        builder.setCustomTitle(headerLayout)

        val scrollView = android.widget.ScrollView(context)
        scrollView.addView(messageView)
        builder.setView(scrollView)

        builder.setPositiveButton("Close", null)
        builder.setNeutralButton(if (isShowingAll) "Trip Only" else "All Stops") { _, _ -> }

        val alertDialog = builder.create()
        alertDialog.show()

        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            isShowingAll = !isShowingAll
            updateContent()
            (it as android.widget.Button).text = if (isShowingAll) "Trip Only" else "All Stops"
        }
    }

}

private fun calculateDuration(startTime: String, endTime: String): String {
    return try {
        // 1. Define the format used in SNTF data (HH:mm)
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())

        // 2. Parse the strings into Date objects
        val date1 = sdf.parse(startTime)
        val date2 = sdf.parse(endTime)

        if (date1 != null && date2 != null) {
            // 3. Calculate difference in milliseconds
            var diff = date2.time - date1.time

            // 4. Handle overnight trips (if arrival is the next day)
            if (diff < 0) {
                diff += 24 * 60 * 60 * 1000
            }

            // 5. Format the output string based on the duration
            val totalMinutes = diff / (1000 * 60)

            if (totalMinutes <= 60) {
                "${totalMinutes}min"
            } else {
                val h = totalMinutes / 60
                val m = totalMinutes % 60
                // Using your preferred String.format for the minutes padding
                "${h}h${String.format("%02d", m)}"
            }
        } else {
            "N/A"
        }
    } catch (e: Exception) {
        "N/A"
    }
}

fun isHolidayTrain(id: String): Boolean {
    return id.contains("H")}
// --- RECYCLERVIEW ADAPTER ---
class TrainAdapter(
    private var list: List<Journey>, // Changed from SearchResult to Journey
    private val onItemClick: (Journey) -> Unit,
    private val onLongClick: (Journey) -> Unit
) : RecyclerView.Adapter<TrainAdapter.VH>(){


    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvId = v.findViewById<TextView>(R.id.tvTrainId)
        val tvRoute = v.findViewById<TextView>(R.id.tvRouteTitle)
        val tvVia = v.findViewById<TextView>(R.id.tvViaStation) // New
        val tvDuration = v.findViewById<TextView>(R.id.tvTotalDuration) // New
        val transferDot = v.findViewById<View>(R.id.transferDot) // New
        val tvDep = v.findViewById<TextView>(R.id.tvDepTime)
        val tvArr = v.findViewById<TextView>(R.id.tvArrTime)
        val tvLineBadge: TextView = v.findViewById(R.id.tvLineBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_journey, parent, false))


    // --- THE UPDATED FUNCTION ---
    override fun onBindViewHolder(holder: VH, position: Int) {
        val journey = list[position]
        val context = holder.itemView.context

        when (journey) {
            is Journey.Direct -> {
                val train = journey.train

                // 1. Header Info (Train ID)
                holder.tvId.text = "Train ${train.id}"
                holder.tvLineBadge.text = journey.train.lineName
                holder.tvLineBadge.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#1976D2")) // Blue for Direct
                holder.tvLineBadge.visibility = View.VISIBLE

                // 2. Route and Duration
                holder.tvRoute.text = "${train.from} ➔ ${train.to}"
                holder.tvDuration.text = calculateDuration(train.depTime, train.arrTime)

                // 3. Hide Transfer-specific UI
                holder.tvVia.visibility = View.GONE
                holder.transferDot.visibility = View.GONE

                // 4. Time and Timeline
                holder.tvDep.text = train.depTime
                holder.tvArr.text = train.arrTime

                // 5. Holiday Logic
                if (train.isHoliday) {
                    holder.tvId.append(" (Hol/Fri)")
                    holder.tvId.setTextColor(Color.RED)
                } else {
                    holder.tvId.setTextColor(Color.parseColor("#6B7280"))
                }
            }

            is Journey.Connection -> {
                // 1. Header Info (Showing both Train IDs)
                holder.tvId.text = "Train ${journey.firstTrain.id} + ${journey.secondTrain.id}"
                holder.tvId.setTextColor(Color.parseColor("#6B7280"))
                holder.tvLineBadge.text = "Connection"
                holder.tvLineBadge.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#374151"))

                // 2. Route and "Via" Label
                holder.tvRoute.text = "${journey.firstTrain.from} ➔ ${journey.secondTrain.to}"
                holder.tvVia.text = "via ${journey.transferStation}"
                holder.tvVia.visibility = View.VISIBLE

                // 3. Timeline and Transfer Dot
                holder.transferDot.visibility = View.VISIBLE
                holder.tvDep.text = journey.firstTrain.depTime
                holder.tvArr.text = journey.secondTrain.arrTime
                holder.tvDuration.text = journey.totalDuration

                // 4. Connection Holiday Logic
                if (journey.firstTrain.isHoliday || journey.secondTrain.isHoliday) {
                    holder.tvId.append(" (Hol/Fri)")
                    holder.tvId.setTextColor(android.graphics.Color.RED)
                }
            }
        }

        // Click Listeners
        holder.itemView.setOnClickListener { onItemClick(journey) }
        holder.itemView.setOnLongClickListener {
            onLongClick(journey)
            true
        }
    }

    override fun getItemCount() = list.size
}

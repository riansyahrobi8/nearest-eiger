package com.dayat.eigerterdekat.model.location;

import java.util.ArrayList;

// kelas untuk menampung data lokasi
public class LocationData {
    public static ArrayList<LocationModel> getLocations(){
        // buat variabel dengan isian berupa array list
        ArrayList<LocationModel> locations = new ArrayList<LocationModel>();

        // tambahkan isi variabel dengan data sesuai model
        locations.add(
                new LocationModel(
                        1,
                        "Eiger Plaza Ambarukmo",
                        "Jl. Laksda Adisucipto, Plaza Ambarukmo Lt. LG A7, Ambarukmo, Caturtunggal, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55581",
                        "",
                        -7.779673693828568,
                        110.40195448175125,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        2,
                        "Eiger Adventure Store",
                        "Jl. Nologaten No.1, RW.02, Ambarukmo, Caturtunggal, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55281",
                        "",
                        -7.778965776236638,
                        110.39991307244276,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        3,
                        "Eiger Ruko Raflesia",
                        "Ruko Raflesia II Blok F, Jl. Babarsari, Caturtunggal, Depok, Kledokan, Caturtunggal, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55281",
                        "",
                        -7.77198766748064,
                        110.41195738736288,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        4,
                        "EIGER GEJAYAN",
                        "Jl. Affandi No.6, Mrican, Caturtunggal, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55222",
                        "",
                        -7.768592709682286,
                        110.39074312620347,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        5,
                        "Eiger Adventure Store Taman Siswa",
                        "Jl. Taman Siswa No. 28, Jl. Taman Siswa No.28, Wirogunan, Kec. Mergangsan, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55151",
                        "",
                        -7.80243843401299,
                        110.37752520017773,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        6,
                        "Eiger YAP Square",
                        "Jl. Yap Square Jl. C. Simanjuntak, Terban, Kec. Gondokusuman, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55223",
                        "",
                        -7.776926835090987,
                        110.37374864988467,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        7,
                        "Eiger Malioboro Mall",
                        "Malioboro Mall, Jl. Malioboro No.52-56, Suryatmajan, Danurejan, Yogyakarta City, Special Region of Yogyakarta 55213",
                        "",
                        -7.7890025189332555,
                        110.36671053342938,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        8,
                        "Eiger Store Jakal Jogja",
                        "Kaliurang St No.KM.5, Manggung, Caturtunggal, Depok Sub-District, Sleman Regency, Special Region of Yogyakarta 55281",
                        "",
                        -7.752944517384355,
                        110.3825033801095,
                        "",
                        1.0
                )
        );
        locations.add(
                new LocationModel(
                        9,
                        "Eiger Store Cokroaminoto",
                        "Pakuncen, Wirobrajan, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55253",
                        "",
                        -7.795635492946743,
                        110.35263430051886,
                        "",
                        1.0
                )
        );

        // kembalikan nilai variablenya
        return locations;
    }
}

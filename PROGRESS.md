# PROGRESS
Terakhir: M-034 selesai | Berikut: M-035

## Aturan sesi
1. Baca file ini lalu MAP.md saja. Detail realisme ada di PROMPT_MASTER.md (di ZIP_LENGKAP); baca bagian yang relevan HANYA jika perlu.
2. Kerjakan mikro-tahap berikut sebanyak yang muat (300-800 token tiap tahap); berhenti SEBELUM token habis.
3. Keluaran = kode + 1 baris status. Tanya dulu jika tidak jelas (maks 3 pertanyaan).
4. Akhir sesi: centang tahap, perbarui MAP.md, kirim (a) M-NNN.zip = proyek LENGKAP dari awal sampai tahap terbaru (NNN = tahap terakhir selesai; pengguna mengganti seluruh repo GitHub dengannya), (b) PAKET_LANJUT.zip baru (PROGRESS.md, MAP.md, file tahap berikut, LANJUT.txt). Jika ada file yang dihapus, tulis di PROGRESS.md baris 'HAPUS: path'.
5. Build via GitHub Actions. Build gagal -> pengguna tempel log -> perbaiki dulu. Claude tidak bisa kompilasi: tulis Kotlin konservatif, tanpa dependensi baru tanpa dicatat.
6. Data nyata: beri tanggal_data + perkiraan=true. Jangan mengarang angka presisi palsu.
7. Jaga file ini <= 60 baris.

## Keputusan tetap
- Target 100 negara (permintaan pengguna): M-028..M-031 menambah 80 negara; semua perkiraan 2026-10
- Game baru dari nol. Kotlin + Jetpack Compose (MVVM). Build GitHub Actions, tanpa Android Studio
- Mata uang Rupiah; kurs asing memengaruhi ekspor/impor
- Modal awal 5 pilihan: Rp20/40/60/80/100 miliar
- 60 usaha + 60 rival swasta (daftar di PROMPT_MASTER.md Lampiran A/B)
- Data: CSV di data/src -> tools/gen_*.py -> JSON di assets/data

## Tahap
- [x] M-001 Gradle + build-apk.yml
- [x] M-002 Struktur folder + MAP.md
- [x] M-003 Skema negara (CSV header + gen_negara.py)
- [x] M-004 negara.csv 10 negara (ID,US,CN,JP,DE,IN,GB,SG,AU,BR); semua perkiraan 2026-10, pajak_dividen = tarif pemotongan ke non-residen, ppn US=0 (pajak penjualan negara bagian belum dimodelkan), upah SG perkiraan (tanpa upah minimum)
- [x] M-005 +10 negara (KR,FR,CA,MY,TH,VN,SA,AE,CH,NL), total 20; perkiraan; AE/CH/SA upah = perkiraan (tanpa upah minimum nasional); CA PPN = GST federal 5%
- [x] M-006 data/Negara.kt (model + dariJson + idrPerMataUang) dan data/NegaraLoader.kt (muatSemua(context)); memakai org.json bawaan Android
- [x] M-007 ui/TemaGame.kt (tema gelap teal+mint) + ui/RankingPdbScreen.kt; MainActivity memuat negara; dependensi baru dicatat: compose foundation (BOM)
- [x] M-008 domain/engine/MakroState.kt (state makro + dari(Negara))
- [x] M-009 domain/engine/MakroEngine.kt tickBulanan (mean reversion + guncangan, seed acak) + test/.../MakroEngineTest.kt (3 uji; numerik diverifikasi di Python, belum dikompilasi)
- [x] M-010 domain/engine/KursEngine.kt (PPP parsial + selisih bunga riil + risk-off; US/SA/AE terpatok) + test/.../KursEngineTest.kt (4 uji; belum dikompilasi)
- [x] M-011 domain/engine/PajakEngine.kt (pphBadan + rugi fiskal 100% tanpa batas waktu, ppnNeto, pajakDividen) + PajakEngineTest (6 uji; belum dikompilasi). Belum: bea masuk/ekspor, pajak karbon/properti, insentif (perlu kolom data baru)
- [x] M-012 domain/engine/LabaRugi.kt (LaporanLabaRugi + LabaRugiEngine.hitung; hpp bank = biaya dana) + LabaRugiEngineTest (3 uji; belum dikompilasi)
- [x] M-013 domain/engine/Neraca.kt (Neraca + seimbang/selisih, NeracaEngine.cekSeimbang) + NeracaTest (3 uji; belum dikompilasi; memakai org.junit)
- [x] M-014 domain/engine/ArusKas.kt (arus kas tidak langsung dari 2 neraca; dividen & belanja modal diturunkan; cocokDenganKas) + ArusKasTest (2 uji; angka diverifikasi manual; belum dikompilasi)
- [x] M-015 domain/engine/Akuntansi.kt (tutupPeriode: neraca awal + laba rugi -> neraca akhir + arus kas; kas turunan) + AkuntansiTest (2 uji; belum dikompilasi)
- [x] M-016 data/src/usaha.csv (kolom: id,nama,kelompok,arketipe,modal_min_usd,hpp_pct,biaya_operasi_pct,penyusutan_pct,pekerja_min,izin;risiko;kpi dipisah ';',tanggal_data,perkiraan; baru 1 baris contoh id 52) + tools/gen_usaha.py (validasi id 1-60, persen 0-100)
- [x] M-017 usaha.csv kelompok A Tambang & Energi (id 1-9); semua perkiraan kasar (modal_min_usd = skala menengah, hpp_pct energi terbarukan = biaya O&M)
- [x] M-018 usaha.csv kelompok B Pertanian (id 10-16); perkiraan kasar
- [x] M-019 usaha.csv Manufaktur C1 (id 17-22); perkiraan kasar
- [x] M-020 usaha.csv Manufaktur C2 (id 23-28); semikonduktor = skala pabrik kecil; perkiraan kasar
- [x] M-021 usaha.csv D Teknologi (id 29-34); perkiraan kasar
- [x] M-022 usaha.csv E Finansial (id 35-40); hpp_pct = biaya dana/klaim, tanpa bahan baku; modal_min = modal inti minimum perkiraan
- [x] M-023 usaha.csv F Logistik (id 41-46)
- [x] M-024 usaha.csv G Properti & Konstruksi (id 47-51); hotel (50) berarketipe ritel_jasa
- [x] M-025 usaha.csv H Ritel & Jasa (id 53-60) -> 60 usaha lengkap. 9 arketipe: tambang_energi, pertanian, manufaktur, teknologi, finansial, logistik, properti_konstruksi, ritel_jasa, layanan_regulasi (56-60)
- [x] M-026 data/Usaha.kt (model + dariJson) + data/UsahaLoader.kt (muatSemua, urut id); dariJson tidak diuji unit (org.json Android tidak tersedia di JVM test); belum dikompilasi
- [x] M-027 domain/usaha/ArketipeUsaha.kt (enum Arketipe, StrukturBiaya, antarmuka ArketipeUsaha, StrategiPersen, ArketipeRegistry.untuk) + test/.../domain/usaha/ArketipeUsahaTest (3 uji; belum dikompilasi)
- [x] M-028 negara.csv +26 Eropa (IT,ES,RU,TR,PL,SE,BE,AT,NO,IE,DK,FI,PT,CZ,GR,RO,HU,SK,UA,LU,BG,RS,LT,SI,LV,EE); BG pakai EUR (2026); upah negara tanpa upah minimum nasional = perkiraan
- [x] M-029 negara.csv +22 Asia & Timur Tengah (PH,PK,BD,HK,TW,KZ,IL,QA,KW,OM,BH,JO,LK,NP,KH,UZ,IQ,AZ,GE,BN,MN,LA); BH pph 0 (hanya sebagian wajib 15%); IQ ppn 0
- [x] M-030 negara.csv +16 Afrika (NG,ZA,EG,KE,ET,GH,MA,DZ,AO,TZ,CI,TN,UG,SN,CM,ZM); data tidak stabil (NG,EG,ET,GH,AO) sangat kasar
- [x] M-031 negara.csv +16 Amerika & Oseania (MX,AR,CO,CL,PE,EC,UY,DO,PA,CR,BO,PY,GT,JM,SV,NZ); AR,BO sangat kasar; total 100 negara
- [x] M-032 KursEngine: TERPATOK diperluas (12 negara), ZONA_EURO (18 negara, acuan DE), TERKAIT (DK,CI,SN,CM->DE paritas tetap; BN->SG), tickSemua(daftar,...) + 4 uji baru di KursEngineTest (belum dikompilasi). Catatan: HR (Kroasia) belum ada di data
- [x] M-033 domain/usaha/StrategiFinansial.kt (nimPct, cadanganDibutuhkan dari NPL x coverage, biayaCadangan, carPct, modalMinimum 8% perkiraan, memenuhiCar; cadangan TIDAK di strukturBiaya, ditambahkan pemanggil) + StrategiFinansialTest (5 uji) + test/.../usaha/ArketipeUsahaTestData.kt (helper uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [x] M-034 domain/usaha/StrategiTambangEnergi.kt (royalti 5% perkiraan masuk biayaOperasi, deplesi unit produksi, tickHarga mean reversion) + StrategiTambangEnergiTest (5 uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [ ] M-035 domain/usaha/StrategiPertanian.kt (musim panen, risiko cuaca/hama, harga komoditas) + uji; daftarkan di ArketipeRegistry | 700

## Bug diketahui
(Build GitHub terbukti berhasil sampai M-012; layar ranking PDB tampil di HP. Jika build gagal, tempel log error.)

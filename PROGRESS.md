# PROGRESS
Terakhir: M-048 selesai | Berikut: M-049

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
- [x] M-001..M-003 Gradle + build-apk.yml, struktur folder + MAP.md, skema negara (CSV header + gen_negara.py)
- [x] M-004..M-005 negara.csv 20 negara (ID,US,CN,JP,DE,IN,GB,SG,AU,BR,KR,FR,CA,MY,TH,VN,SA,AE,CH,NL); perkiraan 2026-10; pajak_dividen = tarif pemotongan non-residen; ppn US=0; upah negara tanpa upah minimum nasional (SG,AE,CH,SA) = perkiraan; CA PPN = GST 5%
- [x] M-006..M-009 data/Negara.kt + NegaraLoader (org.json bawaan Android), ui/TemaGame.kt (tema gelap teal+mint) + RankingPdbScreen, MainActivity memuat negara (dependensi baru: compose foundation via BOM), domain/engine/MakroState.kt, MakroEngine.tickBulanan (mean reversion + guncangan) + MakroEngineTest (3 uji)
- [x] M-010 domain/engine/KursEngine.kt (PPP parsial + selisih bunga riil + risk-off; US/SA/AE terpatok) + test/.../KursEngineTest.kt (4 uji; belum dikompilasi)
- [x] M-011 domain/engine/PajakEngine.kt (pphBadan + rugi fiskal 100% tanpa batas waktu, ppnNeto, pajakDividen) + PajakEngineTest (6 uji; belum dikompilasi). Belum: bea masuk/ekspor, pajak karbon/properti, insentif (perlu kolom data baru)
- [x] M-012..M-015 domain/engine/LabaRugi.kt (hpp bank = biaya dana), Neraca.kt (cekSeimbang), ArusKas.kt (tidak langsung dari 2 neraca; dividen & belanja modal diturunkan), Akuntansi.kt (tutupPeriode; kas turunan) + uji masing-masing (LabaRugiEngineTest 3, NeracaTest 3, ArusKasTest 2, AkuntansiTest 2); belum dikompilasi
- [x] M-016 data/src/usaha.csv (kolom: id,nama,kelompok,arketipe,modal_min_usd,hpp_pct,biaya_operasi_pct,penyusutan_pct,pekerja_min,izin;risiko;kpi dipisah ';',tanggal_data,perkiraan; baru 1 baris contoh id 52) + tools/gen_usaha.py (validasi id 1-60, persen 0-100)
- [x] M-017..M-024 usaha.csv per kelompok: A Tambang&Energi 1-9, B Pertanian 10-16, C Manufaktur 17-28, D Teknologi 29-34, E Finansial 35-40 (hpp=biaya dana/klaim), F Logistik 41-46, G Properti&Konstruksi 47-51 (hotel 50 = ritel_jasa); semua perkiraan kasar
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
- [x] M-035 domain/usaha/StrategiPertanian.kt (pendapatan hanya bulan panen, default Apr+Okt perkiraan; faktorHasil cuaca/hama; hasilPanen) + HargaKomoditas.kt (tick bersama; tickHarga tambang kini mendelegasi) + StrategiPertanianTest (5 uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [x] M-036 domain/usaha/StrategiManufaktur.kt (produksi dibatasi kapasitas, utilisasiPct, biayaPerUnit tetap+variabel, titikImpasUnit) + StrategiManufakturTest (5 uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [x] M-037 domain/usaha/StrategiTeknologi.kt (tickPelanggan dengan churn, pendapatanBerulang, biayaRnd 15% perkiraan, nilaiUmurPelanggan) + StrategiTeknologiTest (5 uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [x] M-038 domain/usaha/StrategiLogistik.kt (kapasitasArmada, utilisasiMuatanPct, biayaBahanBakar, biayaPerTonKm) + StrategiLogistikTest (5 uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [x] M-039 domain/usaha/StrategiPropertiKonstruksi.kt (okupansiPct, pendapatanSewa, progresProyekPct, pendapatanProyek metode persentase penyelesaian) + StrategiPropertiKonstruksiTest (5 uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [x] M-040 domain/usaha/StrategiRitelJasa.kt (pelangganMembeli dari lalu lintas x konversi, pendapatan, perputaranStok, hariPersediaan) + StrategiRitelJasaTest (5 uji); terdaftar di ArketipeRegistry; belum dikompilasi
- [x] M-041 domain/usaha/StrategiLayananRegulasi.kt (tarifEfektif dibatasi plafon regulator, pendapatan, biayaKepatuhan 3% perkiraan, lisensiBerlaku, sisaBulanLisensi) + StrategiLayananRegulasiTest (4 uji); terdaftar di ArketipeRegistry; F6 selesai: 9 arketipe punya strategi khusus; belum dikompilasi
- [x] M-042 (F7) domain/sdm/Kandidat.kt (enum Bidang, enum Jabatan CEO/CFO/COO/CTO/CHRO dengan bobot desain game, Kandidat.skorJabatan, object Rekrutmen: biayaRekrutmen fee agen 20% perkiraan, masaPencarianBulan) + KandidatTest (4 uji); belum dikompilasi
- [x] M-043 (F7) domain/sdm/Penggajian.kt (memenuhiUpahMinimum & sesuaikanKeUpahMinimum memakai Negara.upahMinBulananUsd, totalBiayaKaryawan beban majikan 10% perkiraan, pesangon 1 bulan/tahun perkiraan) + PenggajianTest (3 uji); belum dikompilasi
- [x] M-044 (F7) domain/sdm/Turnover.kt (turnoverBulananPct dari gaji vs median dasar 1.5%/bulan perkiraan, faktorPercobaan x1.5, turnoverEfektifPct, jumlahKeluar acak, biayaPelatihan 20% gaji/bulan perkiraan) + TurnoverTest (4 uji); belum dikompilasi
- [x] M-045 (F7) domain/sdm/Otomatisasi.kt (aktif: timpa perusahaan > aturan holding > bawaan true; efisiensi = rata-rata skor jabatan aktif; faktorBiayaOperasi penghematan maks 10% desain game) + OtomatisasiTest (4 uji); belum dikompilasi
- [x] M-046 (F7) domain/sdm/Kompensasi.kt (bonusKinerja target 20%/ambang 80%/batas 150%, nilaiOpsi intrinsik, opsiVested 48 bln + cliff 12 bln, totalTahunan; semua desain game/perkiraan) + KompensasiTest (4 uji); belum dikompilasi
- [x] M-047 (F7 selesai) domain/sdm/HubunganKerja.kt (tickKepuasan 20%/bulan menuju target dari gaji vs median, peluangMogokBulananPct tanpa serikat = 0 maks 10% desain game, apakahMogok, biayaMogok) + HubunganKerjaTest (4 uji); belum dikompilasi
- [x] M-048 (F8) data/src/aset.csv (kolom: kode,nama,jenis,negara,mata_uang,harga_awal_usd,volatilitas_tahunan_pct,imbal_pct,ukuran_lot,tanggal_data,perkiraan; jenis saham|komoditas|indeks|obligasi|kripto|properti; negara 2 huruf atau GLOBAL) + tools/gen_aset.py (validasi, diuji lokal) + JSON XAU. Baris XAU hanya CONTOH skema, harga 4000 bukan data nyata: isi aset sungguhan dari sumber bertanggal (perkiraan=true)
- [ ] M-049 (F8) data/Aset.kt (model + dariJson) + data/AsetLoader.kt (muatSemua) mengikuti pola Usaha.kt/UsahaLoader.kt | 700

## Bug diketahui
(Build GitHub terbukti berhasil sampai M-012; layar ranking PDB tampil di HP. Jika build gagal, tempel log error.)

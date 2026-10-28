# PROGRESS
Terakhir: M-016 selesai | Berikut: M-017

## Aturan sesi
1. Baca file ini lalu MAP.md saja. Detail realisme ada di PROMPT_MASTER.md (di ZIP_LENGKAP); baca bagian yang relevan HANYA jika perlu.
2. Kerjakan mikro-tahap berikut sebanyak yang muat (300-800 token tiap tahap); berhenti SEBELUM token habis.
3. Keluaran = kode + 1 baris status. Tanya dulu jika tidak jelas (maks 3 pertanyaan).
4. Akhir sesi: centang tahap, perbarui MAP.md, kirim (a) M-NNN.zip = proyek LENGKAP dari awal sampai tahap terbaru (NNN = tahap terakhir selesai; pengguna mengganti seluruh repo GitHub dengannya), (b) PAKET_LANJUT.zip baru (PROGRESS.md, MAP.md, file tahap berikut, LANJUT.txt). Jika ada file yang dihapus, tulis di PROGRESS.md baris 'HAPUS: path'.
5. Build via GitHub Actions. Build gagal -> pengguna tempel log -> perbaiki dulu. Claude tidak bisa kompilasi: tulis Kotlin konservatif, tanpa dependensi baru tanpa dicatat.
6. Data nyata: beri tanggal_data + perkiraan=true. Jangan mengarang angka presisi palsu.
7. Jaga file ini <= 60 baris.

## Keputusan tetap
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
- [ ] M-017 usaha.csv kelompok A Tambang & Energi (id 1-9), perkiraan=true, angka kasar | 700
- [ ] M-018 usaha.csv kelompok B Pertanian (id 10-16) | 700

## Bug diketahui
(Build GitHub belum diuji sama sekali. Jika gagal, tempel log error.)

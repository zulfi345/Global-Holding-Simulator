# PROGRESS
Terakhir: M-058 selesai | Berikut: M-059

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
- [x] M-001..M-015 fondasi: Gradle/build-apk.yml, Negara.kt+Loader, TemaGame+RankingPdbScreen, MakroEngine, KursEngine, PajakEngine, LabaRugi/Neraca/ArusKas/Akuntansi + uji; semua belum dikompilasi
- [x] M-016..M-027 60 usaha lengkap (usaha.csv 9 arketipe: tambang_energi,pertanian,manufaktur,teknologi,finansial,logistik,properti_konstruksi,ritel_jasa,layanan_regulasi) + Usaha.kt/UsahaLoader.kt + ArketipeUsaha.kt (StrukturBiaya, registry)
- [x] M-028..M-031 negara.csv +80 negara (Eropa, Asia&TimTeng, Afrika, Amerika&Oseania) -> total 100 negara; semua perkiraan 2026-10, kasus khusus dicatat per baris di CSV
- [x] M-032 KursEngine: TERPATOK (12), ZONA_EURO (18, acuan DE), TERKAIT (DK,CI,SN,CM->DE; BN->SG), tickSemua + 4 uji. Catatan: HR (Kroasia) belum ada di data
- [x] M-033..M-041 (F6 selesai) strategi per arketipe usaha, masing2 + test (4-5 uji), terdaftar ArketipeRegistry: StrategiFinansial (NIM/cadangan NPL/CAR), StrategiTambangEnergi (royalti/deplesi/harga), StrategiPertanian (musim panen + HargaKomoditas.kt bersama), StrategiManufaktur (kapasitas/titik impas), StrategiTeknologi (churn/pendapatan berulang), StrategiLogistik (armada/bahan bakar), StrategiPropertiKonstruksi (okupansi/progres proyek), StrategiRitelJasa (lalu lintas/konversi), StrategiLayananRegulasi (tarif teregulasi/lisensi); semua belum dikompilasi
- [x] M-042..M-047 (F7 selesai) domain/sdm: Kandidat.kt (jabatan eksekutif CEO/CFO/COO/CTO/CHRO, skorJabatan), Penggajian.kt (upah minimum per negara, pesangon), Turnover.kt (turnover vs gaji median), Otomatisasi.kt (efisiensi dari skor jabatan), Kompensasi.kt (bonus/opsi saham vesting), HubunganKerja.kt (kepuasan/risiko mogok); masing2 + test (3-4 uji); belum dikompilasi
- [x] M-048 (F8) data/src/aset.csv (kolom: kode,nama,jenis,negara,mata_uang,harga_awal_usd,volatilitas_tahunan_pct,imbal_pct,ukuran_lot,tanggal_data,perkiraan; jenis saham|komoditas|indeks|obligasi|kripto|properti; negara 2 huruf atau GLOBAL) + tools/gen_aset.py (validasi, diuji lokal) + JSON XAU. Baris XAU hanya CONTOH skema, harga 4000 bukan data nyata: isi aset sungguhan dari sumber bertanggal (perkiraan=true)
- [x] M-049 (F8) data/Aset.kt (model + dariJson, field kode/nama/jenis/negara/mataUang/hargaAwalUsd/volatilitasTahunanPct/imbalPct/ukuranLot/tanggalData/perkiraan) + data/AsetLoader.kt (muatSemua, urut kode); pola identik Usaha.kt/UsahaLoader.kt; dariJson tidak diuji unit (sama seperti Usaha); belum dikompilasi
- [x] M-050 (F8) domain/engine/HargaAset.kt (tick harga bulanan per aset: mean reversion ke hargaAwal + guncangan skala volatilitasTahunanPct/12, pola dari HargaKomoditas.kt) + HargaAsetTest (4 uji, acuan StrategiTambangEnergiTest); belum dikompilasi
- [x] M-051 (F8) domain/engine/Investasi.kt: beli/jual generik per aset (HasilTransaksiAset; bulatkan ke ukuranLot, unit kurang dari 1 lot diabaikan, biayaBrokerPct+pajakTransaksiPct desain game dari nilaiKotor; beli=nilaiKotor+biaya+pajak, jual=nilaiKotor-biaya-pajak) + InvestasiTest (4 uji); belum dikompilasi
- [x] M-052 (F8) domain/engine/Portofolio.kt: Kepemilikan(kodeAset,unit,hargaRataRata) + Portofolio.tambahBeli (average cost tertimbang dari nilaiBersih HasilTransaksiAset)/kurangiJual (unit tak boleh negatif, hargaRataRata tetap sampai unit 0 lalu reset)/labaRugiBelumTerealisasi + PortofolioTest (5 uji); belum dikompilasi
- [x] M-053 (F8) domain/engine/ImbalAset.kt: bayaranBulanan dari Aset.imbalPct tahunan (dividen/kupon/sewa) atas nilai beli posisi (bukan harga pasar, desain game), imbalPct<=0 -> 0; bayaranNBulan untuk kupon periodik + ImbalAsetTest (4 uji); belum dikompilasi
- [x] M-054 (F8, saham) domain/engine/CircuitBreaker.kt: batasi(hargaBaru,hargaKemarin,batasAtasPct=25,batasBawahPct=25) flat (bukan tiered IDX asli) + kenaAra/kenaArb deteksi; default ARA=ARB=25% + CircuitBreakerTest (6 uji); belum dikompilasi
- [x] M-055 (F8, saham) domain/engine/StockSplit.kt: terapkan(posisi,rasio) -> unit x rasio dibulatkan floor, hargaRataRata/rasio (nilai total tetap, sisa pecahan unit diabaikan bukan cash-in-lieu); rasio<=0 ditolak IllegalArgumentException + StockSplitTest (5 uji); belum dikompilasi
- [x] M-056 (F8, saham) domain/engine/DividenCumEx.kt: berhakDividen(bulanBeli,bulanCum,bulanJual=null) granularitas bulanan (bukan tanggal harian) -> berhak jika beli<=cum DAN (tidak dijual ATAU jual>cum; jual PADA bulanCum = ex-date, tidak berhak) + DividenCumExTest (6 uji); belum dikompilasi
- [x] M-057 (F8, saham SELESAI: lot+biaya+ARA/ARB+cum/ex+split+spread) domain/engine/SpreadBidAsk.kt: hargaBeli(ask)=hargaTengah x (1+spreadPct/200), hargaJual(bid)=hargaTengah x (1-spreadPct/200), half-spread tiap sisi + SpreadBidAskTest (5 uji); belum dikompilasi
- [x] M-058 (F8, obligasi) domain/engine/Obligasi.kt: kuponTahunan(nilaiPar,kuponPct), kuponPerPeriode(...,periodePerTahun), currentYieldPct(kuponTahunan,hargaPasar)=kupon/harga x100 + ObligasiTest (6 uji); belum dikompilasi. Belum: YTM, durasi
- [ ] M-059 (F8, obligasi) Obligasi.kt tambahan: ytmPerkiraanPct(kuponTahunan,nilaiPar,hargaPasar,sisaTahun) = (C+(F-P)/n)/((F+P)/2) x100, rumus aproksimasi YTM standar (bukan iteratif presisi) + uji tambahan

## Bug diketahui
(Build GitHub terbukti berhasil sampai M-012; layar ranking PDB tampil di HP. Jika build gagal, tempel log error.)

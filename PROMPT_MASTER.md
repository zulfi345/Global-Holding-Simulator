# PROMPT MASTER: Global Holding Simulator (Kotlin / Android)

Tempel seluruh isi ini di sesi pertama. Untuk sesi/akun berikutnya cukup tempel ZIP terbaru lalu ketik: **lanjut**.

---

## 1. PERAN & TUJUAN
PROYEK BARU DARI NOL: jangan memakai kode/proyek HTML lama. Tidak ada kompatibilitas save lama.

Kamu adalah arsitek + developer Android senior (Kotlin) sekaligus analis bisnis/keuangan. Bangun game **simulasi strategi & manajemen holding** untuk BELAJAR, bukan hiburan ringan. Keputusan pemain harus punya konsekuensi finansial seperti dunia nyata. Tampilan harus modern, jelas, dan tidak membosankan.

## 2. TEKNOLOGI (WAJIB)
- Kotlin, Jetpack Compose + Material 3, arsitektur MVVM + Clean (domain/data/ui), Hilt untuk DI.
- Room (database lokal), DataStore (pengaturan), Kotlin Coroutines/Flow, kotlinx.serialization.
- Seluruh data dunia nyata disimpan sebagai file JSON di `assets/` (BUKAN hardcode di kode).
- Mesin simulasi = modul Kotlin murni (tanpa dependensi Android) agar bisa diuji unit.
- Grafik: Compose Canvas / Vico. Peta dunia: vektor sederhana.
- Opsional TAHAP AKHIR: AI rival lokal (Qwen 0.5B lewat llama.cpp + JNI). Default: AI rival berbasis aturan/ekonomi. Qwen hanya untuk narasi/berita atau keputusan strategis langka, dan harus bisa dimatikan.
- Python (numpy/pandas) hanya di folder `tools/` untuk menyusun & memvalidasi data JSON, bukan dijalankan di HP.

## 2B. BUILD & UJI LEWAT GITHUB (BUKAN Android Studio)
Pengguna TIDAK memakai Android Studio. Semua build/uji dilakukan GitHub Actions; hasilnya APK.
- Proyek harus langsung bisa di-build di GitHub. M-001 wajib membuat `.github/workflows/build-apk.yml`: JDK 17, `gradle/actions/setup-gradle` dengan versi Gradle tetap (tanpa gradle-wrapper.jar), `gradle assembleDebug` + `gradle test`, lalu unggah APK dan laporan uji sebagai artifact.
- Kamu TIDAK bisa mengompilasi di sini, jadi tulis Kotlin konservatif: dependensi sedikit dan versinya pasti, tanpa API eksperimental, tanpa fitur kompiler baru. Setiap file harus sintaksnya diperiksa teliti sebelum dikirim.
- **Loop perbaikan:** jika build gagal, pengguna menempel 30-50 baris terakhir log error (atau nama file + pesan error). Memperbaikinya = mikro-tahap tersendiri dengan prioritas tertinggi, dan hanya menyentuh file yang disebut error.
- Jangan menambah dependensi baru tanpa mencatatnya di PROGRESS.md (agar build tidak rusak diam-diam).
- Unggahan ke GitHub: pengguna mengunggah ZIP_LENGKAP (isinya diekstrak ke repo) lalu Actions jalan otomatis.

## 3. PROTOKOL SESI (PALING PENTING)
Pengguna memakai Claude free tier dengan token sangat terbatas dan sering ganti akun. Maka:
1. Awal sesi: baca HANYA `PROGRESS.md` lalu `MAP.md`. Jangan membuka semua file.
2. Kerjakan mikro-tahap berikutnya sebanyak yang muat (lihat bagian 11). Sentuh hanya file yang tercantum di PROGRESS.md.
3. Jika tidak jelas, TANYA dulu, jangan menebak. Maksimal 3 pertanyaan.
4. Akhir sesi, selalu: (a) perbarui `PROGRESS.md` (✅/🔄/⏳, keputusan, bug diketahui, tahap berikut + file yang boleh disentuh), (b) perbarui `MAP.md` (file X terhubung ke file Y, fungsi apa), (c) kirim ZIP LENGKAP.
5. Jika token hampir habis, berhenti di titik yang aman: pastikan proyek tetap bisa dibangun, tulis di PROGRESS.md persis di mana berhenti.
6. Jawaban singkat. Jangan menjelaskan panjang; fokus pada kode dan hasil.
7. Jangan klaim "100% bebas bug". Laporkan apa yang diuji dan apa yang belum.

## 4. STRUKTUR PROYEK (satu file = satu tanggung jawab)
```
PROGRESS.md  MAP.md  SPEC.md  CHANGELOG.md
app/src/main/assets/data/
  negara/        (satu JSON per negara: PDB, pertumbuhan, inflasi, suku bunga, kurs, pajak, upah, demografi)
  usaha/         (satu JSON per usaha, ±60 file)
  rival/         (satu JSON per rival, ±50 file)
  aset/          (saham, komoditas, indeks, obligasi, kripto, properti)
  meta/          (sumber data + tanggal, daftar sektor, peristiwa makro)
app/src/main/java/.../
  domain/engine/ (Pajak, Kurs, Harga, Akuntansi, Akuisisi, Rekrutmen, Investasi, Makro, Kredit)
  domain/usaha/  (satu strategi/mekanisme per ARKETIPE usaha)
  domain/rival/  (aturan & AI rival)
  data/          (Room, repository, loader JSON)
  ui/            (satu paket per tab/layar)
tools/           (skrip Python penyusun & validator data)
app/src/test/    (uji unit mesin simulasi)
```
Aturan: tidak ada file > ±400 baris. Data ≠ kode. Setiap file data punya field `sumber` dan `tanggal_data`.

## 5. REALISME (target ≥90% mirip dunia nyata)
Data memakai perkiraan terbaik (±2026), diberi tanda `tanggal_data`. Jika tidak yakin pada angka, beri tanda `perkiraan:true`, jangan mengarang kepastian.

**Mata uang:** game tetap dalam Rupiah. Kurs setiap negara terhadap IDR bergerak dinamis (inflasi relatif, selisih suku bunga, neraca dagang). Kurs memengaruhi biaya impor, pendapatan ekspor, nilai aset asing, dan utang valas.

**Makroekonomi per negara:** PDB, PDB per kapita, pertumbuhan, inflasi (CPI), suku bunga acuan, pengangguran, utang/PDB, siklus bisnis (ekspansi/puncak/resesi/pemulihan). Layar daftar semua negara + peringkat PDB dengan grafik historis. Peristiwa makro (krisis, perang dagang, lonjakan komoditas) memengaruhi sektor berbeda-beda.

**Pajak per negara (berbeda tiap negara):** PPh badan, PPN/VAT/GST, pajak dividen & capital gain, pajak properti, bea masuk/ekspor, pajak karbon, kredit pajak/insentif, perlakuan rugi fiskal, pajak penghasilan karyawan, iuran sosial (BPJS dsb). Pajak holding vs anak usaha, dividen lintas negara, transfer pricing sederhana, risiko audit pajak bila agresif.

**Akuntansi:** laporan laba rugi, neraca, arus kas (operasi/investasi/pendanaan) untuk holding DAN tiap anak usaha. Penyusutan, amortisasi goodwill/impairment, piutang & utang usaha, persediaan (FIFO/rata-rata), modal kerja, EBITDA, margin, ROE/ROIC, DER, ICR.

**Harga:** biaya produksi + margin pasar; elastisitas permintaan; kompetisi rival memengaruhi pangsa pasar & harga; musiman; biaya logistik berdasar jarak/negara; tarif & kurs untuk impor-ekspor. Tidak ada diskon/bonus buatan.

**Tenaga kerja & rekrutmen:** upah minimum & median per negara/kota, kemampuan kandidat berbeda (Pim/Keu/Ops/Tek/SDM) dan memengaruhi hasil nyata, biaya rekrutmen (agen, waktu pencarian), masa percobaan, pelatihan, turnover, pesangon sesuai negara, mogok/serikat, gaji eksekutif + bonus + opsi saham. Jabatan CEO/CFO/COO/CTO/CHRO yang terisi mengotomasi operasional menurut aturan holding (bisa ditimpa per perusahaan).

**Investasi (seperti sungguhan):** saham (lot sesuai bursa: IDX 100 lembar/lot, bursa lain 1 lembar), biaya broker + pajak transaksi per bursa, spread bid-ask, ARA/ARB/circuit breaker, dividen & tanggal cum/ex, stock split, obligasi (kupon, yield, durasi), reksa dana/ETF, komoditas, emas, kripto, properti, deposito. Harga bergerak oleh makro + sektor + berita + volatilitas khas aset (bukan acak murni). Margin/leverage dengan margin call.

**Akuisisi & korporasi:** valuasi rival (kapitalisasi pasar nyata ±2026, dikonversi dari mata uang asalnya), premi akuisisi, due diligence, tender offer, batas kepemilikan asing per negara/sektor, persetujuan antimonopoli, pembiayaan (kas/utang/saham), sinergi nyata dengan risiko integrasi gagal, goodwill, hostile takeover dengan poison pill, IPO/listing, buyback, dividen, divestasi. Rival besar butuh waktu dan modal sangat besar (sesuai ukuran aslinya). Rival juga bisa mengakuisisi pemain lain atau perusahaan pemain.

**Kredit & pendanaan:** bunga mengikuti suku bunga acuan + premi risiko berdasar rasio keuangan, covenant, jadwal cicilan, gagal bayar & restrukturisasi, obligasi korporasi, kredit valas.

## 6. MEKANISME UNIK PER USAHA (≈60 usaha, dikelompokkan jadi arketipe)
Tiap usaha punya file data sendiri DAN memakai strategi arketipenya. Struktur biaya tidak boleh seragam:
- **Bank/Finansial/Asuransi:** tanpa bahan baku. Pendapatan = bunga kredit, fee; biaya = biaya dana (bunga simpanan), cadangan kerugian kredit (NPL), CAR/modal minimum, LDR, kepatuhan. Asuransi: premi, klaim, cadangan.
- **Tambang & Energi:** eksplorasi berisiko, capex besar, deplesi cadangan, royalti, izin, harga komoditas global, AMDAL/pajak karbon.
- **Pertanian:** musim tanam/panen, cuaca, hama, pupuk, harga komoditas, subsidi.
- **Manufaktur:** rantai pasok (bahan baku → barang jadi), kapasitas, utilisasi, persediaan, tarif impor input.
- **Teknologi/Software:** R&D, biaya pengembangan, berlangganan/recurring revenue, churn, biaya server, talenta mahal.
- **Logistik/Transportasi:** armada, BBM, jarak, utilisasi, perawatan.
- **Ritel/Restoran/Hospitality:** sewa, lokasi, bahan baku harian, kapasitas, reputasi, musiman. Rumah makan = usaha terkecil (modal ±Rp20 juta kota kecil, mengikuti kota & negara).
- **Properti/Konstruksi:** tanah, izin, siklus proyek, KPR, okupansi, sewa.
- **Kesehatan/Pendidikan/Media/Telekomunikasi/Utilitas:** regulasi, izin, tarif diatur, capex jaringan, dst.
Daftar usaha = LAMPIRAN A (tepat 60). Setiap usaha: modal awal, struktur biaya tetap/variabel, regulasi, tenaga kerja, risiko khas, KPI.

## 7. RIVAL (±50, satu file per rival)
Daftar rival = LAMPIRAN B (tepat 60, perusahaan SWASTA global + Indonesia, tanpa BUMN/milik negara). Data per rival: (kapitalisasi pasar, pendapatan, margin, utang, sektor, negara, strategi). Perilaku via model ekonomi: ekspansi, perang harga, akuisisi, inovasi, merespons siklus makro dan tindakan pemain. Bersaing sungguhan di pasar yang sama dengan pemain. Nilai bergerak mengikuti kinerja, bukan acak.

## 8. MODAL AWAL
Layar awal: 5 pilihan modal **Rp20 / 40 / 60 / 80 / 100 miliar**. Pilihan lebih kecil = lebih sulit. Tampilkan juga skenario awal (mulai dari mana, negara basis).

## 9. UI/UX
- Material 3, tema gelap teal + aksen mint (bisa diganti), tipografi jelas, terasa seperti aplikasi finansial premium, bukan web.
- Navigasi bawah/laci; dashboard holding: kas, pendapatan, laba bersih, valuasi, arus kas, peringatan risiko.
- Grafik interaktif, tabel ringkas, kartu perusahaan, peta dunia, ticker berita/pasar.
- Setiap angka bisa disentuh untuk melihat rinciannya (mengapa pajak/biaya sebesar itu) agar edukatif.
- Mode belajar: penjelasan singkat istilah, evaluasi akhir periode (apa yang salah/benar), skor keputusan.
- Kecepatan waktu 1x/3x/10x/jeda; simulasi offline mengejar hari yang terlewat (batas wajar).
- Aksesibilitas: ukuran teks, kontras, layar kecil. Performa mulus meski 60 usaha + 50 rival.

## 10. KUALITAS & BUG
- Uji unit untuk: pajak, kurs, akuntansi (neraca harus seimbang), akuisisi, kredit, investasi, rekrutmen.
- Uji simulasi: jalankan 10-20 tahun game tanpa crash; cek kas negatif, pembagian nol, NaN/Infinity, overflow (pakai Long/BigDecimal untuk uang), save/load, migrasi save lama.
- Validator data (`tools/`): semua JSON sesuai skema, tidak ada angka mustahil, sumber & tanggal terisi.
- Tulis hasil uji di `CHANGELOG.md`. Jangan klaim sempurna.

## 11. MIKRO-TAHAP (ANGGARAN TOKEN KETAT)
Jatah per sesi sangat kecil (±1000 token output). Aturan keras:
- Satu mikro-tahap = 1-3 file kecil, target 300-800 token output. Jika ragu muat atau tidak, pecah lagi.
- Kerjakan SEBANYAK mikro-tahap yang muat. Berhenti SEBELUM token habis, jangan mengirim file setengah jadi.
- Data besar (60 usaha, 60 rival, negara) ditulis sebagai CSV ringkas di `data/src/*.csv`; skrip `tools/gen_*.py` mengubahnya jadi JSON. Jangan menulis ratusan JSON manual.
- Jangan membaca ulang file yang tidak ada di daftar "boleh disentuh". Jangan menjelaskan panjang. Keluaran = kode + 1 baris status.
- Tiap mikro-tahap punya ID (M-001, M-002, ...) dan dicatat di PROGRESS.md.

**Fase & urutan (pecah lagi jadi mikro-tahap saat dikerjakan):**
- F1 Kerangka: Gradle + modul, PROGRESS/MAP/SPEC, skema data. (±6 mikro)
- F2 Data negara: CSV 20-30 negara (PDB, inflasi, bunga, kurs, pajak, upah) + generator + layar ranking PDB. (±12)
- F3 Mesin makro & kurs, per komponen. (±10)
- F4 Pajak: satu mikro-tahap per 2-3 negara + uji. (±12)
- F5 Akuntansi: laba rugi, neraca, arus kas, tiap satu mikro + uji. (±8)
- F6 Usaha: CSV 60 usaha dulu; lalu satu mikro per ARKETIPE (9 kelompok). (±25)
- F7 Rekrutmen, jabatan, otomatisasi. (±10)
- F8 Investasi & aset: saham/lot, obligasi, komoditas, dll, satu mikro per jenis. (±15)
- F9 Kredit & pendanaan. (±6)
- F10 Rival: CSV 60 rival, lalu mesin valuasi, lalu AI aturan, lalu akuisisi 2 arah. (±20)
- F11 UI per layar (satu mikro per layar/komponen). (±40)
- F12 Audit: uji unit & simulasi per modul, lalu simulasi 10-20 tahun. (±15)
- F13 Opsional: Qwen 0.5B via llama.cpp (narasi saja). (±8)

**Format PROGRESS.md (wajib dijaga ≤ 60 baris agar murah dibaca):**
```
# PROGRESS
Terakhir: M-0xx selesai | Berikut: M-0yy
## Aturan sesi (ringkas, 5 baris)
## Keputusan tetap (Rupiah, modal 20/40/60/80/100 M, dll)
## Tahap
- [x] M-001 Gradle (build.gradle.kts, settings.gradle.kts)
- [ ] M-002 ... | boleh sentuh: file A, B | anggaran: 600 token
## Bug diketahui
## Catatan penghubung (detail ada di MAP.md)
```
Saat tahap selesai lama, ringkas ke satu baris atau pindah ke CHANGELOG.md agar PROGRESS.md tetap pendek.

## 12. FORMAT KELUARAN (WAJIB tiap akhir sesi: 2 file + 1 baris)
CATATAN: akun baru hanya punya PAKET_LANJUT, bukan seluruh proyek. Maka kirim UPDATE.zip (hanya file baru/berubah, path folder utuh) sebagai pengganti ZIP_LENGKAP, kecuali pengguna mengunggah ZIP_LENGKAP.
1. **ZIP_LENGKAP.zip**: seluruh proyek (arsip/cadangan; dan siap diunggah ke GitHub untuk di-build).
2. **PAKET_LANJUT.zip**: paket kecil untuk sesi berikutnya, berisi HANYA:
   - `PROGRESS.md` (sudah diperbarui, ada tahap berikut + aturan sesi ringkas),
   - `MAP.md`,
   - file-file yang tercantum "boleh disentuh" untuk mikro-tahap berikutnya (dan ketergantungan langsungnya, dari MAP.md),
   - `LANJUT.txt` berisi satu baris perintah untuk pengguna.
3. Baris penutup: "Sesi berikut: unggah PAKET_LANJUT.zip lalu ketik: lanjut".
Kirim kedua file lewat tautan unduh. Jika alat file tidak tersedia, tampilkan isi PAKET_LANJUT sebagai blok teks bertanda nama file.

**Saat pengguna mengetik "lanjut" dengan PAKET_LANJUT.zip:** buka PROGRESS.md, kerjakan mikro-tahap berikut memakai file di dalam paket. Jika perlu file yang tidak ada di paket, minta pengguna mengunggah ZIP_LENGKAP (jangan menebak isinya). Setelah selesai, hasilkan ZIP_LENGKAP + PAKET_LANJUT yang baru. Pengguna selalu memakai PAKET_LANJUT terbaru; ZIP_LENGKAP hanya disimpan sebagai cadangan.

---

## LAMPIRAN A: 60 USAHA YANG BISA DIBANGUN PEMAIN
Satu file JSON per usaha di `data/usaha/`. Kelompok = arketipe mekanisme (bagian 6).

**A. Tambang & Energi (9):** 1 Batu bara, 2 Nikel, 3 Emas, 4 Tembaga, 5 Bauksit & aluminium, 6 Minyak & gas hulu, 7 Tenaga surya (PLTS), 8 Tenaga angin, 9 Panas bumi.
**B. Pertanian & Pangan (7):** 10 Kelapa sawit, 11 Padi & pangan pokok, 12 Kopi & kakao, 13 Karet, 14 Perikanan & budidaya udang, 15 Peternakan unggas, 16 Peternakan sapi.
**C. Manufaktur (12):** 17 Baja, 18 Semen, 19 Petrokimia & plastik, 20 Tekstil & garmen, 21 Furnitur, 22 Otomotif, 23 Elektronik, 24 Semikonduktor, 25 Makanan olahan, 26 Minuman, 27 Farmasi, 28 Kemasan & kertas.
**D. Teknologi (6):** 29 Software/SaaS, 30 Cloud & data center, 31 E-commerce, 32 Fintech pembayaran, 33 Game & hiburan digital, 34 AI & keamanan siber.
**E. Finansial (6):** 35 Bank komersial, 36 Bank digital, 37 Asuransi jiwa, 38 Asuransi umum, 39 Multifinance/leasing, 40 Sekuritas & manajer investasi.
**F. Logistik & Transportasi (6):** 41 Pelayaran kargo, 42 Trucking, 43 Maskapai, 44 Pergudangan & cold chain, 45 Kurir & last-mile, 46 Transportasi kota/ride-hailing.
**G. Properti & Konstruksi (5):** 47 Pengembang perumahan, 48 Properti komersial & mal, 49 Kontraktor infrastruktur, 50 Hotel & resort, 51 Kawasan industri.
**H. Ritel & Jasa (9):** 52 Rumah makan (usaha terkecil), 53 Kafe & waralaba, 54 Minimarket, 55 Supermarket, 56 Rumah sakit & klinik, 57 Sekolah & kampus, 58 Telekomunikasi & menara, 59 Media & hiburan, 60 Air & utilitas.

Tiap file usaha wajib berisi: modal awal minimum per negara, struktur biaya (tetap/variabel; bank TANPA bahan baku), tenaga kerja, regulasi/izin, risiko khas, KPI, dan arketipe mekanismenya.

## LAMPIRAN B: 60 RIVAL (SWASTA, GLOBAL + INDONESIA)
Satu file JSON per rival di `data/rival/`. "Swasta" = dimiliki investor/keluarga/publik, bukan BUMN/milik negara (Saudi Aramco, Mandiri, Telkom, dll. sengaja tidak dimasukkan).

**Global (35):** 1 Apple, 2 Microsoft, 3 Nvidia, 4 Alphabet, 5 Amazon, 6 Meta, 7 Tesla, 8 TSMC, 9 Samsung Electronics, 10 Tencent, 11 Alibaba, 12 ASML, 13 Berkshire Hathaway, 14 JPMorgan Chase, 15 Visa, 16 ExxonMobil, 17 Shell, 18 BHP, 19 Rio Tinto, 20 Glencore, 21 Toyota, 22 Volkswagen, 23 Nestlé, 24 Procter & Gamble, 25 Coca-Cola, 26 Unilever, 27 Walmart, 28 McDonald's, 29 A.P. Møller-Maersk, 30 Johnson & Johnson, 31 Novo Nordisk, 32 Siemens, 33 LVMH, 34 Cargill (swasta tertutup), 35 Koch Industries (swasta tertutup).

**Indonesia (25):** 36 Bank Central Asia (BCA), 37 Astra International, 38 Barito Pacific, 39 Amman Mineral, 40 Chandra Asri, 41 Indofood Sukses Makmur, 42 Indofood CBP, 43 Unilever Indonesia, 44 Golden Agri-Resources (Sinar Mas), 45 Djarum Group (tertutup), 46 Gudang Garam, 47 HM Sampoerna, 48 Kalbe Farma, 49 Mayora Indah, 50 Wings Group (tertutup), 51 Alamtri Resources (ex-Adaro), 52 Medco Energi, 53 Bumi Resources, 54 GoTo, 55 Pakuwon Jati, 56 Ciputra Development, 57 Lippo Karawaci, 58 Charoen Pokphand Indonesia, 59 Japfa Comfeed, 60 Blue Bird.

**Aturan data rival:** setiap rival punya sektor, negara, usaha-usaha yang dijalankan (petakan ke Lampiran A), kapitalisasi pasar/valuasi, pendapatan, margin, utang, kepemilikan, strategi. Angka diisi dari pengetahuan terbaik (±2026) dengan `tanggal_data` + `perkiraan:true`; JANGAN mengarang angka presisi palsu. Perusahaan tertutup: valuasi = estimasi (pendapatan x multiple sektor), tidak bisa dibeli lewat bursa. Daftar ini boleh diperbarui pemain/pengembang tanpa mengubah kode (cukup tambah file JSON).

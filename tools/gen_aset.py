"""CSV -> JSON per aset. Pakai: python tools/gen_aset.py
Baca data/src/aset.csv, tulis app/src/main/assets/data/aset/<kode>.json
jenis: saham|komoditas|indeks|obligasi|kripto|properti. imbal_pct = imbal dividen (saham),
kupon/yield (obligasi), sewa (properti); 0 bila tidak ada. ukuran_lot: lembar per lot
(IDX 100, bursa lain 1). negara: kode 2 huruf atau GLOBAL. harga_awal_usd wajib dari sumber
bertanggal (tanggal_data); tandai perkiraan=true bila tidak pasti."""
import csv, json, os, re, sys

SRC = "data/src/aset.csv"
OUT = "app/src/main/assets/data/aset"
JENIS = {"saham", "komoditas", "indeks", "obligasi", "kripto", "properti"}
TEKS = {"kode", "nama", "jenis", "negara", "mata_uang", "tanggal_data"}
INT = {"ukuran_lot"}
BOOL = {"perkiraan"}

def konversi(k, v):
    v = v.strip()
    if k in TEKS: return v
    if k in INT: return int(v)
    if k in BOOL: return v.lower() in ("1", "true", "ya")
    return float(v)

def main():
    os.makedirs(OUT, exist_ok=True)
    n, kodes = 0, set()
    with open(SRC, newline="", encoding="utf-8") as f:
        for row in csv.DictReader(f):
            if not row.get("kode"): continue
            d = {k: konversi(k, v) for k, v in row.items()}
            kode = d["kode"]
            if not re.fullmatch(r"[A-Za-z0-9_.-]+", kode) or kode in kodes:
                sys.exit("kode aset tidak valid/duplikat: %s" % kode)
            kodes.add(kode)
            if d["jenis"] not in JENIS:
                sys.exit("jenis tidak dikenal: %s (%s)" % (d["jenis"], kode))
            if not (d["negara"] == "GLOBAL" or re.fullmatch(r"[A-Z]{2}", d["negara"])):
                sys.exit("negara harus 2 huruf atau GLOBAL: %s" % kode)
            if d["harga_awal_usd"] <= 0: sys.exit("harga_awal_usd harus > 0: %s" % kode)
            if not 0 <= d["volatilitas_tahunan_pct"] <= 300: sys.exit("volatilitas di luar 0-300: %s" % kode)
            if not 0 <= d["imbal_pct"] <= 100: sys.exit("imbal_pct di luar 0-100: %s" % kode)
            if d["ukuran_lot"] < 1: sys.exit("ukuran_lot harus >= 1: %s" % kode)
            with open(os.path.join(OUT, "%s.json" % kode), "w", encoding="utf-8") as o:
                json.dump(d, o, ensure_ascii=False, indent=1)
            n += 1
    print("aset ditulis:", n)

main()

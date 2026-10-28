"""CSV -> JSON per negara. Pakai: python tools/gen_negara.py
Baca data/src/negara.csv, tulis app/src/main/assets/data/negara/<kode>.json"""
import csv, json, os, sys

SRC = "data/src/negara.csv"
OUT = "app/src/main/assets/data/negara"
TEKS = {"kode", "nama", "mata_uang", "tanggal_data"}
BOOL = {"perkiraan"}

def konversi(k, v):
    v = v.strip()
    if k in TEKS:
        return v
    if k in BOOL:
        return v.lower() in ("1", "true", "ya")
    return float(v)

def main():
    os.makedirs(OUT, exist_ok=True)
    n = 0
    with open(SRC, newline="", encoding="utf-8") as f:
        for row in csv.DictReader(f):
            if not row.get("kode"):
                continue
            d = {k: konversi(k, v) for k, v in row.items()}
            if d["pph_badan_pct"] < 0 or d["pph_badan_pct"] > 60:
                sys.exit("PPh badan tidak wajar: " + d["kode"])
            with open(os.path.join(OUT, d["kode"] + ".json"), "w", encoding="utf-8") as o:
                json.dump(d, o, ensure_ascii=False, indent=1)
            n += 1
    print("negara ditulis:", n)

main()

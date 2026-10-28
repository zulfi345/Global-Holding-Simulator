"""CSV -> JSON per usaha. Pakai: python tools/gen_usaha.py
Baca data/src/usaha.csv, tulis app/src/main/assets/data/usaha/<id>.json
Kolom teks multi-nilai dipisah ';'. hpp_pct: untuk bank/asuransi = biaya dana/klaim."""
import csv, json, os, sys

SRC = "data/src/usaha.csv"
OUT = "app/src/main/assets/data/usaha"
TEKS = {"nama", "kelompok", "arketipe", "tanggal_data"}
LIST = {"izin", "risiko", "kpi"}
INT = {"id", "pekerja_min"}
BOOL = {"perkiraan"}

def konversi(k, v):
    v = v.strip()
    if k in TEKS: return v
    if k in LIST: return [x.strip() for x in v.split(";") if x.strip()]
    if k in INT: return int(v)
    if k in BOOL: return v.lower() in ("1", "true", "ya")
    return float(v)

def main():
    os.makedirs(OUT, exist_ok=True)
    n, ids = 0, set()
    with open(SRC, newline="", encoding="utf-8") as f:
        for row in csv.DictReader(f):
            if not row.get("id"): continue
            d = {k: konversi(k, v) for k, v in row.items()}
            if not 1 <= d["id"] <= 60 or d["id"] in ids:
                sys.exit("id usaha tidak valid/duplikat: %s" % d["id"])
            ids.add(d["id"])
            for k in ("hpp_pct", "biaya_operasi_pct", "penyusutan_pct"):
                if not 0 <= d[k] <= 100: sys.exit("%s di luar 0-100: id %d" % (k, d["id"]))
            if d["modal_min_usd"] <= 0: sys.exit("modal_min_usd harus > 0: id %d" % d["id"])
            with open(os.path.join(OUT, "%02d.json" % d["id"]), "w", encoding="utf-8") as o:
                json.dump(d, o, ensure_ascii=False, indent=1)
            n += 1
    print("usaha ditulis:", n)

main()

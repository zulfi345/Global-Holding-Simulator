"""CSV -> JSON per rival. Pakai: python tools/gen_rival.py
Baca data/src/rival.csv, tulis app/src/main/assets/data/rival/<id>.json
Angka keuangan (USD miliar) opsional: kosong -> null (belum diketahui, bukan 0). Validasi: 60 baris, id 1..60 unik,
arketipe valid, negara ada di negara.csv, status publik|tertutup."""
import csv, json, os, sys

SRC = "data/src/rival.csv"
NEGARA = "data/src/negara.csv"
OUT = "app/src/main/assets/data/rival"
ARKETIPE = {"tambang_energi", "pertanian", "manufaktur", "teknologi", "finansial",
            "logistik", "properti_konstruksi", "ritel_jasa", "layanan_regulasi"}
STATUS = {"publik", "tertutup"}
ANGKA = ["kapitalisasi_usd_miliar", "pendapatan_usd_miliar", "margin_bersih_pct", "utang_usd_miliar"]

def main():
    with open(NEGARA, newline="", encoding="utf-8") as f:
        negara = {r["kode"] for r in csv.DictReader(f)}
    os.makedirs(OUT, exist_ok=True)
    ids = set()
    with open(SRC, newline="", encoding="utf-8") as f:
        for row in csv.DictReader(f):
            if not row.get("id"): continue
            d = {k: v.strip() for k, v in row.items()}
            d["id"] = int(d["id"])
            for k in ANGKA:
                v = d.get(k, "")
                d[k] = float(v) if v != "" else None
                if d[k] is not None and k != "margin_bersih_pct" and d[k] < 0: sys.exit("%s negatif: id %d" % (k, d["id"]))
            if d.get("margin_bersih_pct") is not None and not -100 <= d["margin_bersih_pct"] <= 100:
                sys.exit("margin_bersih_pct di luar -100..100: id %d" % d["id"])
            d["perkiraan"] = d["perkiraan"].lower() in ("1", "true", "ya")
            if not 1 <= d["id"] <= 60 or d["id"] in ids:
                sys.exit("id rival tidak valid/duplikat: %s" % d["id"])
            ids.add(d["id"])
            if d["arketipe_utama"] not in ARKETIPE: sys.exit("arketipe tidak valid: id %d" % d["id"])
            if d["negara"] not in negara: sys.exit("negara tidak ada di negara.csv: %s (id %d)" % (d["negara"], d["id"]))
            if d["status"] not in STATUS: sys.exit("status tidak valid: id %d" % d["id"])
            with open(os.path.join(OUT, "%02d.json" % d["id"]), "w", encoding="utf-8") as o:
                json.dump(d, o, ensure_ascii=False, indent=1)
    if len(ids) != 60: sys.exit("harus tepat 60 rival, ada %d" % len(ids))
    print("rival ditulis:", len(ids))

main()

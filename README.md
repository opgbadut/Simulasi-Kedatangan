# Simulasi Kedatangan Poisson

Aplikasi desktop interaktif berbasis **Java (Swing)** untuk mensimulasikan waktu antar kedatangan (*inter-arrival time*) entitas ke dalam sebuah sistem. Proyek ini memodelkan Proses Poisson menggunakan distribusi eksponensial dan dibangun untuk memenuhi tugas mata kuliah **Simulasi Komputer**.

## Deskripsi Singkat
Program ini menggunakan metode **Inverse Transform Sampling** untuk membangkitkan bilangan acak yang merepresentasikan jeda waktu kedatangan pelanggan. Dengan antarmuka pengguna grafis (GUI), pengguna dapat melihat proses *generate* data secara *real-time* tanpa membuat aplikasi menjadi *freeze* (memanfaatkan *multithreading* dengan `SwingWorker`).

## Fitur Utama
* **Multi-Skenario Arrival Rate ($\lambda$):** Mendukung simulasi tingkat kedatangan 2, 3, dan 6 kedatangan per menit secara bersamaan atau terpisah.
* **Tabel Observasi Real-time:** Menampilkan nomor antrian, skenario, dan jeda waktu kedatangan secara langsung saat simulasi berjalan.
* **Ringkasan Analisis Otomatis:** Menghitung total waktu dan rata-rata *inter-arrival time* untuk setiap skenario secara otomatis di akhir simulasi.
* **UI Responsif:** Dibangun dengan antarmuka Swing yang bersih dan penanganan *error* dasar (misal: validasi input jika tidak ada skenario yang dipilih).

## Konsep Matematika
Simulasi ini menggunakan rumus matematis berikut untuk membangkitkan waktu antar kedatangan:

$$Waktu = \frac{-\ln(1 - U)}{\lambda}$$

* $U$ = Bilangan acak seragam (*Uniform Random Number*) antara 0 dan 1 (didapat dari `Math.random()`).
* $\lambda$ = Tingkat kedatangan (*Arrival Rate*).

## Prasyarat (Prerequisites)
Pastikan komputer Anda sudah terinstal:
* **Java Development Kit (JDK)** versi 8 atau yang lebih baru.

## Cara Menjalankan Program
1. *Clone repository* ini ke komputer Anda:
   ```bash
   git clone [https://github.com/username-anda/simulasi-kedatangan-poisson.git](https://github.com/username-anda/simulasi-kedatangan-poisson.git)

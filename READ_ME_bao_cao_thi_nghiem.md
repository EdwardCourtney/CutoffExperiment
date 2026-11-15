# BẢN TRÌNH BÀY KẾT QUẢ THÍ NGHIỆM 
## Kết quả dạng biểu đồ được trình bày trong results/statisticData.ipynb

## 1. Thông tin sinh viên

- **Họ và tên:** Nguyễn Anh Quân
- **Mã sinh viên:** 25021958
- **Lớp:** 2627I_UET.CS1058_8

## 2. Giới thiệu

Merge Sort có độ phức tạp thời gian Θ(n*log n). Tuy nhiên, trong quá trình thực thi, thuật toán phải thực hiện các thao tác chia mảng, gọi đệ quy và trộn các mảng con. Khi kích thước mảng con nhỏ, chi phí phụ trợ của các thao tác này có thể lớn hơn lợi ích mà việc tiếp tục chia và trộn mang lại.

Một hướng tối ưu là kết hợp Merge Sort với Insertion Sort. Khi kích thước đoạn đang xét không vượt quá một ngưỡng `CUTOFF`, thuật toán chuyển sang sử dụng Insertion Sort để sắp xếp đoạn đó. Hiệu quả của cách kết hợp này phụ thuộc vào giá trị `CUTOFF`, kích thước mảng, đặc điểm dữ liệu đầu vào và môi trường thực thi.

Thí nghiệm được thực hiện nhằm khảo sát mối quan hệ giữa thời gian thực thi và giá trị `CUTOFF` trong nhiều điều kiện đầu vào. Từ đó, thí nghiệm xác định giá trị có thời gian thực thi trung vị thấp nhất trong phạm vi khảo sát cho từng tổ hợp kích thước và kiểu dữ liệu, đồng thời xem xét liệu có một vùng cutoff cho hiệu năng gần tương đương với giá trị tốt nhất hay không.

Sinh viên có tham khảo AI trong quá trình thực hiện dự án. Sinh viên tự xây dựng thuật toán sắp xếp và khung đo đạc; sử dụng AI để hỗ trợ hoàn thiện một số đoạn mã đo đạc và xây dựng các lệnh Python phục vụ thống kê, trực quan hóa kết quả.

- **Đoạn chat AI được sử dụng:** [ChatGPT – Cutoff Experiment](https://chatgpt.com/share/6af68cc6-a28c-83ec-9997-bc9ef3210b42)


## 3. Thiết kế thí nghiệm

### 3.1. Thuật toán và cách đo thời gian

Chương trình sử dụng `System.nanoTime()` để đo thời gian thực thi của lời gọi `mergeSort(0, A.length - 1)`. Trong hàm `measureEach()` của `Experiment.java`, đối tượng `Sort` được khởi tạo trước khi bắt đầu đo; thời điểm kết thúc được ghi lại ngay sau khi thuật toán sắp xếp hoàn thành.

Các thao tác sao chép mảng đầu vào và kiểm tra mảng đã được sắp xếp nằm ngoài khoảng thời gian đo. Vì vậy, thời gian được ghi nhận tập trung vào quá trình thực thi thuật toán Merge Sort kết hợp Insertion Sort, bao gồm các thao tác cấp phát mảng phụ trong quá trình trộn.

### 3.2. Làm nóng JVM (warm-up)

Mã nguồn hiện tại thực hiện hai giai đoạn warm-up:

- **Warm-up ban đầu:** sử dụng một mảng `RANDOM` có kích thước 100.000 phần tử và thực hiện 500 lần chạy trước khi bắt đầu các phép đo chính (`BIGWARMUP = 500`). Các giá trị cutoff trong giai đoạn này được lấy theo thứ tự xáo trộn.
- **Warm-up cho từng dataset:** chương trình thực hiện `max(WARMUP, MAX_CUTOFF)` lần chạy. Với `WARMUP = 30` và `MAX_CUTOFF = 150`, số lần warm-up thực tế cho mỗi dataset là 150. Do thứ tự cutoff là một hoán vị từ 1 đến 150, mỗi cutoff được chạy một lần trong giai đoạn này.

Các lần warm-up không được ghi vào file kết quả CSV. Lưu ý rằng `WARMUP = 30` là giá trị cấu hình tối thiểu; số lần chạy thực tế đang được quyết định bởi giá trị lớn hơn giữa `WARMUP` và `MAX_CUTOFF`.

### 3.3. Kiểu dữ liệu đầu vào

Thí nghiệm sử dụng bốn kiểu mảng, được tạo trong `ArrayManager.java`:

| Kiểu dữ liệu | Cách tạo |
|---|---|
| `RANDOM` | Tạo mảng gồm các số nguyên từ 0 đến n−1, sau đó xáo trộn ngẫu nhiên. |
| `SORTED` | Tạo mảng gồm các số nguyên từ 0 đến n−1 theo thứ tự tăng dần. |
| `REVERSED` | Tạo mảng gồm các số nguyên từ n−1 đến 0 theo thứ tự giảm dần. |
| `NEARLY` | Bắt đầu từ mảng tăng dần, sau đó thực hiện n/10 lần hoán đổi ngẫu nhiên giữa hai vị trí. |


### 3.4. Kích thước mảng và phạm vi CUTOFF

Có bốn kích thước mảng được khảo sát:

- N = 1.000
- N = 10.000
- N = 100.000
- N = 1.000.000

Với mỗi dataset, chương trình khảo sát 150 giá trị `CUTOFF`, từ 1 đến 150. Trong mỗi trial, thứ tự thử các giá trị CUTOFF được xáo trộn; mỗi giá trị được đo một lần trong trial đó.

Thứ tự sinh dataset, thứ tự kích thước, kiểu dữ liệu và thứ tự thử CUTOFF được điều khiển bằng các bộ sinh số giả ngẫu nhiên có seed. Seed mặc định trong mã nguồn là `20261114L`; `Launcher.java` cũng cho phép truyền seed khác qua tham số dòng lệnh.

### 3.5. Cấu hình thí nghiệm

Các hằng số trong `src/main/java/Experiment.java` trên nhánh `master` tại thời điểm đối chiếu:

| Tham số | Giá trị | Ý nghĩa |
|---|---:|---|
| `BIGWARMUP` | 500 | Số lần warm-up ban đầu trên mảng RANDOM kích thước 100.000. |
| `WARMUP` | 30 | Giá trị warm-up cấu hình tối thiểu cho mỗi dataset; số lần chạy thực tế là 150 do `MAX_CUTOFF = 150`. |
| `MAX_CUTOFF` | 150 | Số giá trị CUTOFF được khảo sát, từ 1 đến 150. |
| `TRIAL` | 5 | Số trial trên mỗi dataset. |
| `DATASET` | 15 | Số dataset cho mỗi tổ hợp kích thước và kiểu dữ liệu. |


## 4. Thành phần Project

Các thành phần chính trong repository:

| Đường dẫn | Chức năng |
|---|---|
| `src/main/java/ArrayManager.java` | Sinh mảng theo các kiểu dữ liệu, xáo trộn và sao chép mảng. |
| `src/main/java/DataManager.java` | Lưu các quan sát và xuất dữ liệu ra `results/result.csv`. |
| `src/main/java/Experiment.java` | Thiết lập warm-up, đo thời gian và lặp qua kích thước, kiểu dữ liệu, dataset, trial và cutoff. |
| `src/main/java/Sort.java` | Cài đặt Merge Sort kết hợp Insertion Sort theo ngưỡng CUTOFF. |
| `src/main/java/Type.java` | Khai báo bốn kiểu dữ liệu đầu vào. |
| `src/main/java/Launcher.java` | Điểm bắt đầu chương trình; nhận seed tùy chọn, chạy thí nghiệm và xuất CSV. |
| `results/result.csv` | Dữ liệu đo thời gian thực thi. |
| `results/statisticData.ipynb` | Đọc CSV, tổng hợp thống kê và vẽ biểu đồ. |

## 5. Báo cáo đo đạc

### 5.1. Mô tả dữ liệu

File `results/result.csv` được đối chiếu có **180.000 quan sát** (không tính dòng tiêu đề), với các cột:

`size,id_type,dataset,cutoff,trial,time_ns`

Ý nghĩa các cột:

| Cột | Ý nghĩa |
|---|---|
| `size` | Kích thước mảng đầu vào. |
| `id_type` | Mã kiểu dữ liệu: 1 = RANDOM, 2 = NEARLY, 3 = REVERSED, 4 = SORTED. |
| `dataset` | Mã định danh dataset. |
| `cutoff` | Giá trị CUTOFF được thử. |
| `trial` | Chỉ số trial, gồm các giá trị 0, 1, 2, 3, 4. |
| `time_ns` | Thời gian thực thi tính bằng nanosecond. |

Thống kê cấu trúc CSV:

- 4 kích thước mảng: 1.000; 10.000; 100.000; 1.000.000.
- 4 kiểu dữ liệu đầu vào.
- 240 mã dataset trên toàn bộ file; mỗi tổ hợp kích thước–kiểu dữ liệu có 15 dataset.
- 5 trial trên mỗi dataset.
- 150 giá trị CUTOFF (từ 1 đến 150).
- Mỗi tổ hợp kích thước–kiểu dữ liệu–cutoff có 75 quan sát (15 dataset × 5 trial).

Số quan sát ghi nhận là:

4 kích thước mảng × 4 kiểu dữ liệu × 15 dataset cho mỗi tổ hợp × 150 cutoff × 5 trial = 180,000 quan sát

Số lượng quan sát trong CSV khớp với cấu hình `DATASET = 15`, `TRIAL = 5` và `MAX_CUTOFF = 150` trong mã nguồn.

### 5.2. Phương pháp thống kê trong notebook

Notebook `results/statisticData.ipynb` chuyển `time_ns` sang millisecond bằng cách chia cho 1.000.000. Sau đó, notebook áp dụng **cùng một quy trình tổng hợp cho cả bốn kiểu dữ liệu**:

1. Với từng tổ hợp `size`, `id_type`, `cutoff` và `dataset`, tính trung vị của 5 trial.
2. Với từng tổ hợp `size`, `id_type` và `cutoff`, tính trung vị của 15 giá trị trung vị dataset ở bước 1.

Notebook tạo bốn biểu đồ, mỗi biểu đồ ứng với một kích thước mảng. Trên mỗi biểu đồ có bốn đường biểu diễn thời gian thực thi trung vị theo CUTOFF, tương ứng với bốn kiểu dữ liệu đầu vào.



### 5.3. CUTOFF có thời gian trung vị thấp nhất trong phạm vi khảo sát

Từ CSV và theo đúng phương pháp median hai tầng của notebook mới, các giá trị CUTOFF có thời gian trung vị thấp nhất trong dải khảo sát 1–150 như sau:

| Kích thước mảng | RANDOM | NEARLY | REVERSED | SORTED |
|---:|---:|---:|---:|---:|
| 1.000 | 12 | 32 | 8 | 125 |
| 10.000 | 106 | 80 | 5 | 102 |
| 100.000 | 66 | 83 | 11 | 134 |
| 1.000.000 | 35 | 57 | 13 | 145 |

Thời gian trung vị tương ứng tại các cutoff trên:

| Kích thước mảng | RANDOM (ms) | NEARLY (ms) | REVERSED (ms) | SORTED (ms) |
|---:|---:|---:|---:|---:|
| 1.000 | 0,0106 | 0,0080 | 0,0107 | 0,0033 |
| 10.000 | 0,3745 | 0,1327 | 0,1483 | 0,0667 |
| 100.000 | 5,5641 | 2,2855 | 1,9243 | 0,9226 |
| 1.000.000 | 70,1253 | 28,4523 | 23,2946 | 11,4313 |

Các giá trị trên là cutoff có thời gian trung vị thấp nhất **trong phạm vi 1–150 và theo CSV hiện tại**. Chúng không khẳng định đó là giá trị tối ưu tuyệt đối cho mọi máy tính hoặc mọi bộ dữ liệu.


### 5.4. Cấu hình máy tính và môi trường thực thi

Thông tin cấu hình máy tính được ghi nhận khi thực hiện thí nghiệm:

| Thành phần | Thông tin |
|---|---|
| CPU | AMD Ryzen AI 7 H 350 w/ Radeon 860M (2.00 GHz) |
| CPU Cores | 8 |
| Logical Processors | 16 |
| RAM | 32.0 GB (31.3 GB usable) |
| Operating System | Windows 11 Home 26200.9974 |
| JDK runtime được ghi nhận | Oracle JDK 25 |
| JVM được ghi nhận | Java HotSpot 64-Bit Server VM 25.0.2+10-LTS-69 |
| IDE | IntelliJ IDEA 2025.3.3 |



## 6. Đánh giá thí nghiệm

### 6.1. Nhận xét về kết quả

Kết quả cho thấy thời gian thực thi của Merge Sort kết hợp Insertion Sort thay đổi theo giá trị CUTOFF. Việc lựa chọn CUTOFF phù hợp giúp cân bằng giữa chi phí chia, gọi đệ quy và trộn mảng của Merge Sort với chi phí sắp xếp các đoạn nhỏ bằng Insertion Sort.

Không có một giá trị CUTOFF duy nhất đạt thời gian trung vị thấp nhất trong cả 16 tổ hợp kích thước và kiểu dữ liệu. Theo kết quả tổng hợp hiện tại, cutoff tốt nhất của REVERSED lần lượt là 8, 5, 11 và 13; trong khi SORTED có các giá trị 125, 102, 134 và 145. Điều này cho thấy kết quả phụ thuộc vào đặc điểm dữ liệu đầu vào và kích thước mảng.



### 6.2. Ảnh hưởng của đặc điểm dữ liệu

Với dữ liệu SORTED hoặc NEARLY, các đoạn nhỏ được đưa vào Insertion Sort thường có ít thao tác dịch chuyển phần tử hơn so với dữ liệu ngẫu nhiên hoặc đảo ngược. Vì vậy, Insertion Sort có thể xử lý hiệu quả các đoạn có thứ tự tốt.

Ngược lại, với dữ liệu REVERSED, các đoạn nhỏ có thể cần nhiều thao tác đổi chỗ hơn. Kết quả đo đạc hiện tại cho thấy các cutoff tốt nhất của REVERSED tương đối thấp so với SORTED. Đây là kết quả thực nghiệm trên cách tạo dữ liệu và môi trường đã nêu, không nên khái quát thành quy luật tuyệt đối cho mọi bộ dữ liệu.

### 6.3. Kết luận

Thí nghiệm cho thấy việc kết hợp Merge Sort và Insertion Sort thông qua ngưỡng CUTOFF có ảnh hưởng đến thời gian thực thi. Giá trị CUTOFF có thời gian trung vị thấp nhất thay đổi theo kích thước và đặc điểm của mảng đầu vào.

Kết quả hiện tại không ủng hộ việc chọn một cutoff duy nhất làm giá trị tối ưu cho mọi trường hợp. Đồng thời, số lượng cutoff có hiệu năng gần tốt nhất ở một số tổ hợp cho thấy việc xác định một vùng cutoff ổn định có thể có ý nghĩa thực tiễn hơn việc chỉ chọn một điểm có thời gian đo thấp nhất.

Do đó, khi triển khai thuật toán, việc lựa chọn CUTOFF cần căn cứ vào kích thước mảng, đặc điểm dữ liệu và môi trường thực thi. Các kết quả trong báo cáo nên được hiểu là kết quả thực nghiệm trên cấu hình và phạm vi khảo sát đã nêu.

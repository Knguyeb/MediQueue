package com.khoinguyen.mediqueue.config;

import com.khoinguyen.mediqueue.entity.BacSi;
import com.khoinguyen.mediqueue.entity.BenhNhan;
import com.khoinguyen.mediqueue.entity.BenhVien;
import com.khoinguyen.mediqueue.entity.DanhMucChuyenKhoa;
import com.khoinguyen.mediqueue.entity.QuanLy;
import com.khoinguyen.mediqueue.repository.BenhVienRepository;
import com.khoinguyen.mediqueue.repository.DanhMucChuyenKhoaRepository;
import com.khoinguyen.mediqueue.service.BacSiService;
import com.khoinguyen.mediqueue.service.BenhNhanService;
import com.khoinguyen.mediqueue.service.QuanLyService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class SeedData implements CommandLineRunner {

    private final BenhVienRepository benhVienRepository;
    private final DanhMucChuyenKhoaRepository danhMucChuyenKhoaRepository;
    
    // Inject thêm 3 Service để tự động sinh Tài khoản cho người dùng
    private final QuanLyService quanLyService;
    private final BacSiService bacSiService;
    private final BenhNhanService benhNhanService;

    public SeedData(BenhVienRepository benhVienRepository, 
                    DanhMucChuyenKhoaRepository danhMucChuyenKhoaRepository,
                    QuanLyService quanLyService, 
                    BacSiService bacSiService, 
                    BenhNhanService benhNhanService) {
        this.benhVienRepository = benhVienRepository;
        this.danhMucChuyenKhoaRepository = danhMucChuyenKhoaRepository;
        this.quanLyService = quanLyService;
        this.bacSiService = bacSiService;
        this.benhNhanService = benhNhanService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Chỉ thêm dữ liệu mẫu nếu bảng benh_vien đang trống
        if (benhVienRepository.count() == 0) {
            System.out.println("Đang khởi tạo dữ liệu mẫu cho bảng Bệnh Viện...");

            BenhVien bv1 = new BenhVien();
            bv1.setTenBenhVien("Bệnh viện Chợ Rẫy");
            bv1.setDiaChi("201B Nguyễn Chí Thanh, Quận 5, TP.HCM");
            bv1.setSoDienThoai("028 3855 4137");
            bv1.setEmail("choray@mediqueue.vn");
            bv1.setViDo(10.7573);
            bv1.setKinhDo(106.6575);
            bv1.setHinhAnh("choray1.jpg;choray2.jpg");

            BenhVien bv2 = new BenhVien();
            bv2.setTenBenhVien("Bệnh viện Đại học Y Dược");
            bv2.setDiaChi("215 Hồng Bàng, Quận 5, TP.HCM");
            bv2.setSoDienThoai("028 3855 4269");
            bv2.setEmail("ydhc@mediqueue.vn");
            bv2.setViDo(10.7550);
            bv2.setKinhDo(106.6631);
            bv2.setHinhAnh("daihocyduoc1.jpg;daihocyduoc2.jpg");

            BenhVien bv3 = new BenhVien();
            bv3.setTenBenhVien("Bệnh viện Bạch Mai");
            bv3.setDiaChi("78 Giải Phóng, Phương Mai, Đống Đa, Hà Nội");
            bv3.setSoDienThoai("024 3869 3731");
            bv3.setEmail("bachmai@mediqueue.vn");
            bv3.setViDo(21.0000);
            bv3.setKinhDo(105.8402);
            bv3.setHinhAnh("bachmai1.jpg;bachmai2.jpg");

            BenhVien bv4 = new BenhVien();
            bv4.setTenBenhVien("Bệnh viện Đa khoa Tâm Anh");
            bv4.setDiaChi("2B Phổ Quang, Tân Bình, TP.HCM");
            bv4.setSoDienThoai("028 7102 6789");
            bv4.setEmail("tamanh@mediqueue.vn");
            bv4.setViDo(10.8037);
            bv4.setKinhDo(106.6664);
            bv4.setHinhAnh("tamanh1.jpg;tamanh2.jpg");

            BenhVien bv5 = new BenhVien();
            bv5.setTenBenhVien("Bệnh viện Hữu nghị Việt Đức");
            bv5.setDiaChi("40 Tràng Thi, Hoàn Kiếm, Hà Nội");
            bv5.setSoDienThoai("024 3825 3531");
            bv5.setEmail("vietduc@mediqueue.vn");
            bv5.setViDo(21.0287);
            bv5.setKinhDo(105.8471);
            bv5.setHinhAnh("vietduc1.jpg;vietduc2.jpg");

            BenhVien bv6 = new BenhVien();
            bv6.setTenBenhVien("Bệnh viện Trung ương Quân đội 108");
            bv6.setDiaChi("1 Trần Hưng Đạo, Hai Bà Trưng, Hà Nội");
            bv6.setSoDienThoai("069 572 400");
            bv6.setEmail("bv108@mediqueue.vn");
            bv6.setViDo(21.0197);
            bv6.setKinhDo(105.8614);
            bv6.setHinhAnh("bv108_1.jpg");

            BenhVien bv7 = new BenhVien();
            bv7.setTenBenhVien("Bệnh viện Đa khoa Trung ương Cần Thơ");
            bv7.setDiaChi("315 Nguyễn Văn Linh, An Khánh, Ninh Kiều, Cần Thơ");
            bv7.setSoDienThoai("0292 3820 071");
            bv7.setEmail("bvdktwcantho@mediqueue.vn");
            bv7.setViDo(10.0216);
            bv7.setKinhDo(105.7469);
            bv7.setHinhAnh("bvdktwcantho1.jpg");

            BenhVien bv8 = new BenhVien();
            bv8.setTenBenhVien("Bệnh viện Đại học Y Dược Cần Thơ");
            bv8.setDiaChi("179 Nguyễn Văn Cừ, An Khánh, Ninh Kiều, Cần Thơ");
            bv8.setSoDienThoai("0292 3899 444");
            bv8.setEmail("bvyduoccantho@mediqueue.vn");
            bv8.setViDo(10.0278);
            bv8.setKinhDo(105.7573);
            bv8.setHinhAnh("bvyduoccantho1.jpg");

            BenhVien bv9 = new BenhVien();
            bv9.setTenBenhVien("Bệnh viện Đa khoa Trung tâm An Giang");
            bv9.setDiaChi("60 Ung Văn Khiêm, Mỹ Phước, Thành phố Long Xuyên, An Giang");
            bv9.setSoDienThoai("0296 3852 543");
            bv9.setEmail("bvdkangiang@mediqueue.vn");
            bv9.setViDo(10.3644);
            bv9.setKinhDo(105.4371);
            bv9.setHinhAnh("bvdkangiang1.jpg");

            BenhVien bv10 = new BenhVien();
            bv10.setTenBenhVien("Bệnh viện Đa khoa Đà Nẵng");
            bv10.setDiaChi("124 Hải Phòng, Thạch Thang, Hải Châu, Đà Nẵng");
            bv10.setSoDienThoai("0236 3821 118");
            bv10.setEmail("bvdadanang@mediqueue.vn");
            bv10.setViDo(16.0742);
            bv10.setKinhDo(108.2155);
            bv10.setHinhAnh("bvdadanang1.jpg");

            // Lưu toàn bộ 10 bệnh viện và lấy danh sách trả về để dùng cho Quản lý/Bác sĩ
            List<BenhVien> danhSachBenhVien = Arrays.asList(bv1, bv2, bv3, bv4, bv5, bv6, bv7, bv8, bv9, bv10);
            List<BenhVien> savedBenhViens = benhVienRepository.saveAll(danhSachBenhVien);

            // =====================================================================
            // TẠO DỮ LIỆU CHUYÊN KHOA
            // =====================================================================
            List<DanhMucChuyenKhoa> savedChuyenKhoas = danhMucChuyenKhoaRepository.findAll();
            
            if (savedChuyenKhoas.isEmpty()) {
                System.out.println("Đang khởi tạo dữ liệu mẫu cho bảng Danh Mục Chuyên Khoa...");
                DanhMucChuyenKhoa ck1 = new DanhMucChuyenKhoa(); ck1.setTenDanhMuc("Nội khoa");
                DanhMucChuyenKhoa ck2 = new DanhMucChuyenKhoa(); ck2.setTenDanhMuc("Ngoại khoa");
                DanhMucChuyenKhoa ck3 = new DanhMucChuyenKhoa(); ck3.setTenDanhMuc("Sản phụ khoa");
                DanhMucChuyenKhoa ck4 = new DanhMucChuyenKhoa(); ck4.setTenDanhMuc("Nhi khoa");
                DanhMucChuyenKhoa ck5 = new DanhMucChuyenKhoa(); ck5.setTenDanhMuc("Tai mũi họng");
                DanhMucChuyenKhoa ck6 = new DanhMucChuyenKhoa(); ck6.setTenDanhMuc("Răng hàm mặt");
                DanhMucChuyenKhoa ck7 = new DanhMucChuyenKhoa(); ck7.setTenDanhMuc("Da liễu");
                DanhMucChuyenKhoa ck8 = new DanhMucChuyenKhoa(); ck8.setTenDanhMuc("Tim mạch");
                DanhMucChuyenKhoa ck9 = new DanhMucChuyenKhoa(); ck9.setTenDanhMuc("Thần kinh");
                DanhMucChuyenKhoa ck10 = new DanhMucChuyenKhoa(); ck10.setTenDanhMuc("Cơ xương khớp");

                List<DanhMucChuyenKhoa> listCk = Arrays.asList(ck1, ck2, ck3, ck4, ck5, ck6, ck7, ck8, ck9, ck10);
                savedChuyenKhoas = danhMucChuyenKhoaRepository.saveAll(listCk);
            }

            // =====================================================================
            // TẠO DỮ LIỆU NGƯỜI DÙNG TỰ ĐỘNG
            // =====================================================================
            System.out.println("Đang tự động khởi tạo Quản lý, Bác sĩ, Bệnh nhân và cấp Tài khoản...");
            
            Random random = new Random();
            String[] hoList = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng"};
            String[] tenDemList = {"Văn", "Thị", "Hoàng", "Minh", "Thanh", "Ngọc", "Thảo", "Quốc", "Gia", "Bảo"};
            String[] tenList = {"Anh", "Tuấn", "Hương", "Trang", "Khoa", "Phát", "Linh", "Hà", "Long", "Đạt"};

            // 1. Duyệt qua từng bệnh viện để thêm 1 Quản lý và 2 Bác sĩ
            for (BenhVien bv : savedBenhViens) {
                // --- THÊM 1 QUẢN LÝ ---
                QuanLy ql = new QuanLy();
                ql.setHoTen(hoList[random.nextInt(10)] + " " + tenDemList[random.nextInt(10)] + " " + tenList[random.nextInt(10)]);
                ql.setSoDienThoai("09" + (10000000 + random.nextInt(90000000))); // Random SĐT 10 số
                ql.setEmail("quanly." + bv.getMaBenhVien() + "@mediqueue.vn");
                ql.setGioiTinh(random.nextBoolean());
                ql.setBenhVien(bv);
                // Dùng service để lưu (Nó sẽ tự cấp tên đăng nhập và mật khẩu '123')
                quanLyService.themMoiQuanLy(ql); 

                // --- THÊM 2 BÁC SĨ ---
                for (int i = 0; i < 2; i++) {
                    BacSi bs = new BacSi();
                    bs.setHoTen(hoList[random.nextInt(10)] + " " + tenDemList[random.nextInt(10)] + " " + tenList[random.nextInt(10)]);
                    bs.setSoDienThoai("09" + (10000000 + random.nextInt(90000000)));
                    bs.setEmail("bacsi" + i + "." + bv.getMaBenhVien() + "@mediqueue.vn");
                    bs.setGioiTinh(random.nextBoolean());
                    bs.setBenhVien(bv);
                    // Chọn ngẫu nhiên 1 chuyên khoa từ danh sách 10 chuyên khoa
                    bs.setChuyenKhoa(savedChuyenKhoas.get(random.nextInt(savedChuyenKhoas.size())));
                    
                    bacSiService.themMoiBacSi(bs);
                }
            }

            // 2. Tạo 3 Bệnh nhân (Không thuộc về bệnh viện nào)
            for (int i = 1; i <= 3; i++) {
                BenhNhan bn = new BenhNhan();
                bn.setHoTen(hoList[random.nextInt(10)] + " " + tenDemList[random.nextInt(10)] + " " + tenList[random.nextInt(10)]);
                bn.setSoDienThoai("09" + (10000000 + random.nextInt(90000000)));
                bn.setEmail("benhnhan" + i + "@gmail.com");
                bn.setGioiTinh(random.nextBoolean());
                // Sinh ngày sinh ngẫu nhiên từ năm 1980 đến 2010
                bn.setNgaySinh(LocalDate.of(1980 + random.nextInt(30), 1 + random.nextInt(11), 1 + random.nextInt(27)));
                bn.setDiaChi("Thành phố Hồ Chí Minh");
                
                benhNhanService.themMoiBenhNhan(bn);
            }

            System.out.println("============== HOÀN TẤT SEED DATA ==============");
            System.out.println("Đã thêm: 10 Bệnh viện, 10 Chuyên khoa, 10 Quản lý, 20 Bác sĩ, 3 Bệnh nhân");
            System.out.println("Lưu ý: Mật khẩu mặc định của tất cả tài khoản đều là '123'");
        }
    }
}
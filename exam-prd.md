
# Online Eğitim ve Sınav Platformu – PRD

## 🧩 Genel Tanım

Bu proje, öğrencilerin çevrimiçi kurslara katılabildiği, eğitmenlerin içerik oluşturabildiği ve sınavların yönetildiği bir mikroservis mimarili sistemdir. Amaç, *mikroservisler arası iletişim*, *veritabanı ilişkileri*, *event-driven yapı*, *saga* ve *outbox pattern* gibi konuları gerçek senaryolarla öğrenmektir.

## 👤 Aktörler

- **Öğrenci**
  - Kurslara katılır
  - Sınavlara girer
- **Eğitmen**
  - Kurs oluşturur
  - Kurs içeriklerini yönetir
  - Sınav tanımlar
- **Sistem**
  - Event'leri işler
  - Saga üzerinden adım adım işlemleri yönetir

## 🏛️ Mikroservisler

### 1. Config Server
- Tüm mikroservislerin merkezi konfigürasyon yönetimi
- Spring Cloud Config Server kullanımı
- Git tabanlı yapılandırma deposu
- Her servis için farklı profiller (dev, test, prod)
- API:
  - GET /{application}/{profile}
  - GET /{application}/{profile}/{label}

### 2. Service Discovery (Eureka Server)
- Servis keşif ve yönetimi
- Dinamik servis kaydı ve bulunması
- Yük dengeleme için hazırlık
- Servis sağlamlık kontrolü (health checks)

### 3. User Service
- Kullanıcı kayıt ve doğrulama
- Kullanıcı rolleri: STUDENT, INSTRUCTOR
- Veritabanı: PostgreSQL
- API:
  - POST /users
  - GET /users/{id}

### 4. Course Service
- Kurs oluşturma ve güncelleme
- Kursa eğitmen atanması
- Öğrencilerin kursa kayıt olması
- Veritabanı: PostgreSQL
- API:
  - POST /courses
  - POST /courses/{courseId}/enroll
- İlişkiler:
  - OneToMany: Kurs → Sınav
  - ManyToOne: Kurs → Eğitmen
  - ManyToMany: Kurs ↔ Öğrenciler

### 5. Exam Service
- Her kurs için sınav tanımlama
- Öğrenci sınav kayıt ve cevap işlemleri
- Sınav sonuçlarının hesaplanması
- Veritabanı: MongoDB
- API:
  - POST /exams
  - POST /exams/{id}/submit
- Event Listener: Öğrenci kursa kayıt olduğunda sınav kaydını otomatik oluştur

## 🔁 Saga Pattern Kullanımı

**Senaryo:** Öğrenci bir kursa kayıt olurken;
1. UserService: Öğrenci var mı, uygun mu?
2. CourseService: Kursa kayıt işlemi
3. ExamService: RabbitMQ üzerinden event tetiklenir → otomatik sınav oluşturulur
4. Herhangi bir adım başarısızsa rollback işlemi (compensating transaction)

## 📦 Outbox Pattern Kullanımı

**Senaryo:** Öğrenci sınavı tamamladığında;
1. ExamService → sonuç MongoDB’ye kaydedilir
2. Outbox tablosuna event yazılır
3. Worker veya Debezium bu eventi alır → diğer servislere (CourseService gibi) gönderir

## 🛠️ Teknoloji ve Araçlar

| Alan | Teknoloji |
|--|--|
| Backend | Spring Boot |
| DB | PostgreSQL (User & Course), MongoDB (Exam) |
| Mesajlaşma | RabbitMQ |
| Servis İletişimi | REST + Async Events |
| Konfigürasyon | Spring Cloud Config Server |
| Servis Keşfi | Netflix Eureka |
| Containerization | Docker + Docker Compose |
| Transaction Mgmt | Saga Pattern |
| Event Delivery | Outbox Pattern |

## 🧪 Test Senaryoları (Sample)

- ✅ Öğrenci kayıt olur, bir kursa kaydolur, sınav oluşturulur → başarılı saga akışı
- ❌ Öğrenci kayıtlı değil → saga fail + rollback
- ❌ RabbitMQ down → Outbox pattern devrede, event sonra gönderilir


## 🔄 Config Server ve Service Discovery Yapısı

### Config Server
- **Yapılandırma Deposu**: GitHub/GitLab üzerinde merkezi repo
- **Profil Yapısı**: 
  - `user-service-dev.yml`
  - `course-service-dev.yml`
  - `exam-service-dev.yml`
- **Özellikler**: Şifreleme desteği, profil bazlı yapılandırma, otomatik yenileme

### Eureka Server
- **Yapı**: Tek instance (ölçeklenebilir)
- **Özellikler**: 
  - Servis kaydı
  - Servis durumu izleme
  - Dinamik servis bulma
  - Servisler arası iletişimde isim bazlı erişim



## 📁 Yol Haritası (Roadmap)

1. [ X ] Config Server kurulumu + Git repo yapılandırması
2. [ X ] Eureka Server kurulumu
3. [ X ] User Service kurulumu + Docker + PostgreSQL + Eureka Client
4. [ X ] Course Service ve ilişki yapılarının oluşturulması + Eureka Client
5. [ x ] Exam Service ve MongoDB entegrasyonu + Eureka Client
6. [ x ] RabbitMQ event yapılandırması
7. [ ] Saga yönetimi (manuel state-based flow)
8. [ X ] Outbox tablosu ve event dispatch mekanizması
9. [ ] API Gateway (isteğe bağlı)
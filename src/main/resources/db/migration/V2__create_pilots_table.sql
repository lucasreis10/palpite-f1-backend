CREATE TABLE pilots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_id VARCHAR(50) NOT NULL UNIQUE,
    given_name VARCHAR(100) NOT NULL,
    family_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    nationality VARCHAR(50) NOT NULL,
    url VARCHAR(500),
    permanent_number INT,
    code VARCHAR(3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_driver_id (driver_id),
    INDEX idx_nationality (nationality),
    INDEX idx_family_name (family_name)
); 
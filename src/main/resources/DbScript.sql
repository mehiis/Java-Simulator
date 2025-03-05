CREATE DATABASE TRASH_SIMULATOR;
USE TRASH_SIMULATOR;

CREATE TABLE inputs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    simulation_time INT NOT NULL,
    mean_trash_per_throw DOUBLE NOT NULL,
    truck_arrival_interval INT NOT NULL,
    single_apt_amount INT NOT NULL,
    double_apt_amount INT NOT NULL,
    triple_apt_amount INT NOT NULL,
    quad_apt_amount INT NOT NULL,
    mixed_amount INT NOT NULL,
    plastic_amount INT NOT NULL,
    bio_amount INT NOT NULL,
    glass_amount INT NOT NULL,
    paper_amount INT NOT NULL,
    metal_amount INT NOT NULL
);

CREATE USER 'simulatoruser'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE ON trash_simulator.* TO 'simulatoruser'@'localhost';
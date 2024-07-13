DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS book_request;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS hotel;
DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    email varchar(70)
);

CREATE TABLE IF NOT EXISTS hotel (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    location varchar(50),
    name varchar(50),
    phone_number varchar(15),
    img varchar(350),
    owner_id bigint,
    CONSTRAINT hotel_user_fk FOREIGN KEY (owner_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS room(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    nr_guests int,
    price decimal(10, 2),
    img varchar(350),
    hotel_id bigint,
    CONSTRAINT room_hotel_fk FOREIGN KEY (hotel_id) REFERENCES hotel (id)
);

CREATE TABLE IF NOT EXISTS book_request (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    end_date datetime(6),
    start_date datetime(6),
    guest_id bigint,
    room_id bigint,
    moderator_id bigint,
    CONSTRAINT book_request_moderator_fk FOREIGN KEY (moderator_id) REFERENCES user (id),
    CONSTRAINT book_request_guest_fk FOREIGN KEY (guest_id) REFERENCES user (id),
    CONSTRAINT book_request_room_fk FOREIGN KEY (room_id) REFERENCES room (id)
);

CREATE TABLE IF NOT EXISTS booking (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    end_date datetime(6),
    start_date datetime(6),
    status varchar(50),
    moderator_id bigint,
    guest_id bigint,
    room_id bigint,
    CONSTRAINT booking_moderator_fk FOREIGN KEY (moderator_id) REFERENCES user (id),
    CONSTRAINT booking_guest_fk FOREIGN KEY (guest_id) REFERENCES user (id),
    CONSTRAINT booking_room_fk FOREIGN KEY (room_id) REFERENCES room (id)
);


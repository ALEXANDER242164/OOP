package com.informaticonfing.spring.app.springboot.repository;

import com.informaticonfing.spring.app.springboot.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}

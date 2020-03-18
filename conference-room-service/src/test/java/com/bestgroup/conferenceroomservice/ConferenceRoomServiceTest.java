package com.bestgroup.conferenceroomservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConferenceRoomServiceTest {

    @InjectMocks
    ConferenceRoomService conferenceRoomService;

    @Mock
    ConferenceRoomRepository conferenceRoomRepository;

    @Test
    public void getExistingRoomByIdReturnRoom(){

        Optional<ConferenceRoom> conferenceRoom = Optional.of(new ConferenceRoom(1, "TEST", 13));

        when(conferenceRoomRepository.findById(any())).thenReturn(conferenceRoom);

        ConferenceRoom conferenceRoomReturned = conferenceRoomService.getRoomById(1);

        assertThat(conferenceRoomReturned.getFloor()).isEqualTo(1);
        assertThat(conferenceRoomReturned.getName()).isEqualTo("TEST");

    }

    @Test
    public void getNonExistingRoomByIdReturnResourceNotFoundException(){

        Optional<ConferenceRoom> conferenceRoom = Optional.ofNullable(null);
        when(conferenceRoomRepository.findById(any())).thenReturn(conferenceRoom);

       assertThrows(ResourceNotFoundException.class, () ->conferenceRoomService.getRoomById(12));

    }

    @Test
    public void getExistingAllRoomsReturnListOfRooms(){

        ArrayList conferenceRoomList = new ArrayList();
        ConferenceRoom conferenceRoom1 = new ConferenceRoom(1, "TEST", 13);
        ConferenceRoom conferenceRoom2 = new ConferenceRoom(2, "TEST", 15);
        conferenceRoomList.add(conferenceRoom1);
        conferenceRoomList.add(conferenceRoom2);

        when(conferenceRoomRepository.findAll()).thenReturn(conferenceRoomList);

        ArrayList conferenceRoomListReturned = (ArrayList) conferenceRoomService.getAllRooms();

        assertThat(conferenceRoomListReturned.size()).isEqualTo(2);

    }

    @Test
    public void getNonExistingAllRoomsReturnEmptyList(){

        List<ConferenceRoom> conferenceRoomList = new ArrayList<>();

        when(conferenceRoomRepository.findAll()).thenReturn(conferenceRoomList);

        ArrayList conferenceRoomListReturned = (ArrayList) conferenceRoomService.getAllRooms();

        assertThat(conferenceRoomListReturned.size()).isEqualTo(0);

    }

    @Test
    public void updateExistingRoomUpdateSuccessfully(){

        ConferenceRoom conferenceRoom1 = new ConferenceRoom(1, "TEST", 13);
        ConferenceRoom conferenceRoom2 = new ConferenceRoom(2, "TEST", 15);

        when(conferenceRoomRepository.findById(any())).thenReturn(java.util.Optional.of(conferenceRoom1));
        when(conferenceRoomRepository.save(conferenceRoom1)).thenReturn(conferenceRoom2);

        ConferenceRoom conferenceRoomReturned = conferenceRoomService.update(1,conferenceRoom1);

        assertThat(conferenceRoomReturned.getFloor()).isEqualTo(2);
        assertThat(conferenceRoomReturned.getSize()).isEqualTo(15);

    }

    @Test
    public void deleteRoom(){

        conferenceRoomService.deleteRoomById(1);

       verify(conferenceRoomRepository,times(1)).deleteById(1);

    }

}


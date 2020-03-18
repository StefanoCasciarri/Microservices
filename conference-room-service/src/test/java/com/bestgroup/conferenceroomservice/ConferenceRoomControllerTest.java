package com.bestgroup.conferenceroomservice;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConferenceRoomControllerTest {

    @InjectMocks
    ConferenceRoomController conferenceRoomController;

    @Mock
    ConferenceRoomService conferenceRoomService;


    @Test
    public void getAllRoomsReturnEmptyList(){

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));

        when(conferenceRoomService.getAllRooms()).thenReturn(new ArrayList<ConferenceRoom>());

        ResponseEntity<List<ConferenceRoom>> responseEntity = conferenceRoomController.getAllRooms();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().isEmpty()).isTrue();

    }

    @Test
    public void getAllRoomsReturnNonEmptyList(){

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));


        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"TEST",15);
        ArrayList conferenceRoomsList = new ArrayList<>();
        conferenceRoomsList.add(conferenceRoom);


        when(conferenceRoomService.getAllRooms()).thenReturn(conferenceRoomsList);

        ResponseEntity<List<ConferenceRoom>> responseEntity = conferenceRoomController.getAllRooms();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().size()).isEqualTo(1);
        assertThat(responseEntity.getBody().get(0).getFloor()).isEqualTo(1);

    }

    @Test
    public void getExistingRoomByIdReturnRoom(){

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));


        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"TEST",15);


        when(conferenceRoomService.getRoomById(any())).thenReturn(conferenceRoom);

        ResponseEntity<ConferenceRoom> responseEntity = conferenceRoomController.getRoomById(1);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().getFloor()).isEqualTo(1);
        assertThat(responseEntity.getBody().getName()).isEqualTo("TEST");

    }

    @Test
    public void createNewRoom(){

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));

        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"TEST",15);

        when(conferenceRoomService.createConferenceRoom(any())).thenReturn(conferenceRoom);

        ResponseEntity<ConferenceRoom> responseEntity = conferenceRoomController.create(conferenceRoom);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }


    @Test
    public void updateRoom(){

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));

        ConferenceRoom conferenceRoom = new ConferenceRoom(1,"TEST",15);

        when(conferenceRoomService.update(any(),any())).thenReturn(conferenceRoom);

        ResponseEntity<ConferenceRoom> responseEntity = conferenceRoomController.update(1,conferenceRoom);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }


}

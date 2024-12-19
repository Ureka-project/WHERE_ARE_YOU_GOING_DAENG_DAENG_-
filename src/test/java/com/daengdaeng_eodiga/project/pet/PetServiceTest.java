package com.daengdaeng_eodiga.project.pet;

import com.daengdaeng_eodiga.project.Global.exception.*;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.pet.dto.PetListResponseDto;
import com.daengdaeng_eodiga.project.pet.dto.PetRegisterDto;
import com.daengdaeng_eodiga.project.pet.dto.PetUpdateDto;
import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.pet.repository.PetRepository;
import com.daengdaeng_eodiga.project.pet.service.PetService;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommonCodeService commonCodeService;

    private User sampleUser;
    private Pet samplePet;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);

        sampleUser = User.builder()
                .userId(1)
                .nickname("testUser")
                .email("user1@example.com")
                .gender("GND_01")
                .city("광주")
                .cityDetail("광산구")
                .oauthProvider("google")
                .build();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = dateFormat.parse("2020-12-24");
        samplePet = Pet.builder()
                .image("https://example.com/pet1.png")
                .size("PET_SIZ_01")
                .user(sampleUser)
                .birthday(birthday)
                .gender("GND_02")
                .name("Buddy")
                .neutering(true)
                .species("PET_TYP_10")
                .build();
        samplePet.setPetId(1);
    }

    @Test
    void fetchUserPets_ShouldReturnPets() {
        when(petRepository.findAllByUser(sampleUser)).thenReturn(List.of(samplePet));

        List<Pet> pets = petService.fetchUserPets(sampleUser);

        assertNotNull(pets);
        assertEquals(1, pets.size());
        assertEquals("Buddy", pets.get(0).getName());

        verify(petRepository, times(1)).findAllByUser(sampleUser);
    }

    @Test
    void registerPet_ShouldSavePet() {
        PetRegisterDto requestDto = PetRegisterDto.builder()
                .name("Buddy")
                .species("PET_TYP_10")
                .gender("GND_02")
                .size("PET_SIZ_01")
                .birthday("2024-01-01")
                .neutering(false)
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));

        petService.registerPet(1, requestDto);

        verify(commonCodeService, times(1)).isCommonCode("PET_TYP_10");
        verify(commonCodeService, times(1)).isCommonCode("GND_02");
        verify(commonCodeService, times(1)).isCommonCode("PET_SIZ_01");
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void fetchUserPetListDto_ShouldReturnPetListResponseDto() {
        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(petRepository.findAllByUser(sampleUser)).thenReturn(List.of(samplePet));
        when(commonCodeService.getCommonCodeName(anyString())).thenReturn("Friendly Name");

        List<PetListResponseDto> result = petService.fetchUserPetListDto(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Buddy", result.get(0).getName());

        verify(petRepository, times(1)).findAllByUser(sampleUser);
    }

    @Test
    void deletePet_ShouldDeletePet() {
        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(petRepository.findById(1)).thenReturn(Optional.of(samplePet));

        petService.deletePet(1, 1);

        verify(petRepository, times(1)).delete(samplePet);
    }

    @Test
    void deletePet_ShouldThrowException_WhenPetNotOwnedByUser() {
        User anotherUser = User.builder()
                .userId(2)
                .nickname("testUser2")
                .email("user2@example.com")
                .gender("GND_02")
                .city("대전")
                .cityDetail("대덕구")
                .oauthProvider("google")
                .build();
        samplePet.setUser(anotherUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(petRepository.findById(1)).thenReturn(Optional.of(samplePet));

        assertThrows(UserUnauthorizedException.class, () -> petService.deletePet(1, 1));
        verify(petRepository, never()).delete(samplePet);
    }

    @Test
    void updatePet_ShouldUpdatePet() {
        PetUpdateDto updateDto = PetUpdateDto.builder()
                .name("Updated Buddy")
                .species("PET_TYP_09")
                .gender("GND_01")
                .size("PET_SIZ_02")
                .birthday("2024-01-01")
                .neutering(true)
                .build();

        when(petRepository.findById(1)).thenReturn(Optional.of(samplePet));

        petService.updatePet(1, updateDto);

        verify(commonCodeService, times(1)).isCommonCode("PET_TYP_09");
        verify(commonCodeService, times(1)).isCommonCode("GND_01");
        verify(commonCodeService, times(1)).isCommonCode("PET_SIZ_02");
        verify(petRepository, times(1)).save(samplePet);

        assertEquals("Updated Buddy", samplePet.getName());
    }
}

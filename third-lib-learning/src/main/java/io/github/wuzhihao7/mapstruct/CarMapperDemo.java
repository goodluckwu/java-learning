package io.github.wuzhihao7.mapstruct;

import java.util.Objects;

public class CarMapperDemo {
    public static void main(String[] args) {
        Car car = new Car("Morris", 5, CarType.SEDAN);

        CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);

        System.out.println(car);
        System.out.println(carDto);

        assert Objects.nonNull(carDto);
        assert "Morris".equals(carDto.getMake());
        assert 5 == carDto.getSeatCount();
        assert "SEDAN".equals(carDto.getType());
    }
}

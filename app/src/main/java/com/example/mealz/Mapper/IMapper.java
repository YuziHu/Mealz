package com.example.mealz.Mapper;

public interface IMapper<From, To> {
    To map(From from);
}

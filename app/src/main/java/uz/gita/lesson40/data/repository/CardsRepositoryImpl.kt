package uz.gita.lesson40.data.repository

import retrofit2.Response
import uz.gita.lesson40.data.datasource.CardsDataSourse
import uz.gita.lesson40.domain.entity.AddCardEntity
import uz.gita.lesson40.domain.entity.CardResponse
import uz.gita.lesson40.domain.entity.getResponse.GetCardsesponse
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(private val dataSourse: CardsDataSourse):CardsRepository{
    override suspend fun addCards(addCardEntity: AddCardEntity, bearerToken: String): Response<CardResponse> {
        return dataSourse.addCards(addCardEntity,bearerToken)
    }

    override suspend fun getCards(bearerToken: String): GetCardsesponse {
        return dataSourse.getCards(bearerToken)
    }



}
package com.account.manage.accountmanage.accountbook.application.port.`in`

import com.account.manage.accountmanage.accountbook.model.AccountBookRequest
import com.account.manage.accountmanage.accountbook.model.AccountBookResponse
import com.account.manage.accountmanage.user.domain.User

interface AccountInfoUpdateUseCase {

    fun updateAccountInfo(accountBookRequest: AccountBookRequest, accountBookId: Long, user: User): AccountBookResponse

    fun deleteAccount(accountBookId: Long, user: User): AccountBookResponse
}
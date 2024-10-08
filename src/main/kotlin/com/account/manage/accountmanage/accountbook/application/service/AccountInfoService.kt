package com.account.manage.accountmanage.accountbook.application.service

import com.account.manage.accountmanage.accountbook.adapter.out.error.AccountBookErrorType
import com.account.manage.accountmanage.accountbook.adapter.out.persistence.AccountBookRepository
import com.account.manage.accountmanage.accountbook.application.port.`in`.AccountInfoUseCase
import com.account.manage.accountmanage.accountbook.model.*
import com.account.manage.accountmanage.accountbook.model.AccountBooksInfoDto.Companion.toAccountBooksInfoDto
import com.account.manage.accountmanage.common.adpater.out.error.DataNotFoundException
import com.account.manage.accountmanage.common.infra.auth.UserExtractor
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountInfoService(
    private val accountBookRepository: AccountBookRepository,
    private val permissionCheckService: PermissionCheckService,
) : AccountInfoUseCase {

    @Transactional(readOnly = true)
    override fun getAccountInfo(accountBookRequest: AccountBookRequest): AccountBookResponse {
        val accountBookDetailDto = accountBookRequest as AccountBookDetailDto
        val user = accountBookDetailDto.user
        val accountBookId = accountBookDetailDto.accountBookId

        val accountBook = (accountBookRepository.findAccountBookById(accountBookId)
            ?: throw DataNotFoundException(AccountBookErrorType.NOT_FOUND_ACCOUNT_BOOK))

        permissionCheckService.checkAccountBookPermission(user, accountBook)

        return AccountDetailInfoDto(
            ownerEmail = accountBook.user.email,
            memo = accountBook.accountInfo.memo,
            amount = accountBook.accountInfo.amount,
            updatedTime = accountBook.updatedAt,
        )
    }

    @Transactional(readOnly = true)
    override fun getAccountBookList(accountBookRequest: AccountBookRequest): AccountBookResponse {
        val accountBooksDto = accountBookRequest as AccountBooksDto

        val accountBooks = accountBookRepository.findAccountBooksByUserId(
            pageable = accountBooksDto.pageable,
            userId = accountBooksDto.user.id,
        )

        return AccountBooksWrapperDto(
            accountBooks.content.map {
                it.toAccountBooksInfoDto()
            }
        )
    }
}
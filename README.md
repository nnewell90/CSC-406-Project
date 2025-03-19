# Banking System Project

## Project Overview
This project models a banking system that manages different types of accounts, transactions, and loans. The system interacts with a database to store and process financial information while allowing customers, tellers, and managers to perform various banking operations. 

## Table of Contents
 1. Features
 2. System Components
 3. Actors and Their Roles
 4. Transaction Workflow
 5. Overdraft Protection
 6. Interest Rate Adjustments
 7. Penalties and Fees
 8. Setup and Configuration
 9. Limitations

## Features
* Savings Accounts: Allow deposits, withdrawals, and interest acrual.
* Checking Accounts: Includes "That's My Bank" (TMB) and "Gold/Diamond" accounts with different transaction fees and benefits.
* Certificate of Deposit (CDs): Fixed-term deposits with penalties for early withdrawals and automatic rollovers.
* Loans: Supports mortgage loans (15/30-year), short-term loans (e.g. car loans), and credit cards.
* ATM Transactions: Limits on withdrawals per day and ensures customers cannot overdraw.
* Overdraft Protection: Transfer funds from savings to checking if enabled.
* Management Functions: Interest rate adjustments, credit/debit of accounts, and loan processing.

## System Components
1. Banking System: The core application handling transactions and processing requests.
2. Database: Stores account details, transactions, and customer information.
3. Actors: Customers, Tellers, and Managers interact with the system.

## Actors and Their Roles

| Actor | Capabilities |
| ----- | ------------ |
| Customer | Deposits, withdrawals, transfers, loan payments, credit card usage |
| Teller | Access balances, process transactions, manage transfers |
| Manager | Adjust interest rates, manage loans, handle overdrafts, process fees |

## Transaction Workflow
Example: Customer Withdraws Money
1. Customer requests a withdrawal.
2. Banking System checks balance.
3. If funds are sufficient, the system deducts the amount and dispenses cash.
4. If funds are insufficient, the system checks for overdraft protection.
5. If overdraft protection exists, it transfers funds from savings.
6. Transaction is recorded in the database.

## Overdraft Protection
* If a checking account has overdraft protection enabled, funds are automatically transferred from savings to cover insufficient balances.
* If overdraft protection is not enabled, a $25 fee is charged for a returned check.

## Interest Rate Adjustments
* Savings accounts accrue a fixed daily compounded interest rate.
* Gold/Diamond checking accounts earn 0.5 x the savings rate, but only if the minimum $5,000 balance is maintained.
* Managers can update interest rates, which automatically affects all relevant accounts.

## Penalties and Fees

| Transaction | Fee |
| ------ | ---- |
| Stop Payment Request | $35 per check |
| Overdraft (without protection) | $25 per check |
| TMB Account Transactions | $0.75 per transaction |
| Monthly Transfers (TMB Account) | $1.25 per transaction |
| Late Loan Payment | $75 per month |

## Setup and Configuration
1. Database Setup
    * Configure tables for accounts, transactions, loans, and customers.
    * Ensure indexes are set on primary keys for fast lookups (SSNs).
2. Banking System Implementation
    * Implement account operations (withdraw, deposit, transfer).
    * Implement loan management (payment processing, late fee handling).
    * Implement credit card processing (purchase validation, monthly billing).
3. User Access Control
    * Customers should only see their own accounts.
    * Tellers should have broader access but no administrative controls.
    * Managers should have full access, including loan processing and interest rate changes.

## Limitations
* The system currently does not support multi-currency transactions.
* Fraud detection is not implemented but should be considered for real-world applications.
* ATM withdrawals are limited to two per day, which may need adjustments for business requirements.

## Conclusion
This banking system efficiently manages customer transactions, loans, and accounts while enforcing penalties, fees, and overdraft protection. The system is designed to ensure accurate financial operations while providing flexibility for customers, tellers, and managers. 

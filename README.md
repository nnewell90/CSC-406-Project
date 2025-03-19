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


package main

import (
	"fmt"
)

// --------------

type Wallet struct {
	Cash int
}

func (w *Wallet) Pay(amount int) error {
	if w.Cash < amount {
		return fmt.Errorf("Не хватает денег в кошельке")
	}
	w.Cash -= amount
	return nil
}

func (w *Wallet) Put(amount int) error {
	w.Cash += amount
	return nil
}

// --------------

type Card struct {
	Balance    int
	ValidUntil string
	Cardholder string
	CVV        string
	Number     string
}

func (c *Card) Pay(amount int) error {
	if c.Balance < amount {
		return fmt.Errorf("Не хватает денег на карте")
	}
	c.Balance -= amount
	return nil
}

func (c *Card) Put(amount int) error {
	c.Balance += amount
	return nil
}

// --------------

type ApplePay struct {
	Money   int
	AppleID string
}

func (a *ApplePay) Pay(amount int) error {
	if a.Money < amount {
		return fmt.Errorf("Не хватает денег на аккаунте")
	}
	a.Money -= amount
	return nil
}

func (a *ApplePay) Put(amount int) error {
	a.Money += amount
	return nil
}

// --------------

type Payer interface {
	Pay(int) error
	Put(int) error
}

// --------------

func Buy(p Payer) {
	err := p.Pay(10)
	if err != nil {
		fmt.Printf("Ошибка при оплате %T: %v\n\n", p, err)
		return
	}
	fmt.Printf("Спасибо за покупку через %T\n\n", p)
}

func Put(p Payer) {
	p.Put(50)
	fmt.Printf("Спасибо за пополнение через %T\n\n", p)
	return
}

// --------------

func main() {

	myWallet := &Wallet{Cash: 100}
	Buy(myWallet)

	var myMoney Payer
	myMoney = &Card{Balance: 100, Cardholder: "rvasily"}
	Buy(myMoney)

	myMoney = &ApplePay{Money: 9}
	Buy(myMoney)
	Put(myMoney)
	Buy(myMoney)
}

package com.training.shoplocal.passwordview

class Password(private var password: String="") {
    private var animated = false
    fun getPassword() = password
    fun isAnimated() = animated
    fun changeChar(value: Char){
        animated = false
        if (value == '<') {
            if (password.isNotEmpty()){
                password = password.dropLast(1)
            }
        } else
            if (password.length < 5) {
                animated = true
                password += value
/*                if (password.length == 5) {

                }*/
            }
    }
}
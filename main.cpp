//
//  main.cpp
//  mcts--
//
//  Created by 虞晨曦 on 2018/12/17.
//  Copyright © 2018年 虞晨曦. All rights reserved.
//
#include <iostream>
#include <ctime>
#include <stdint.h>
#include "ai.h"
using namespace std;

/*
 * main function, 作为Java与AI接口。
 */
int main(int argc, const char * argv[]) {
    if (argc != 66){
        return 1;
    }
    int i = 0;
    if (atoi(argv[1]) == 1) {
        mctsmm4::selfcolor = 1;
    }else{
        mctsmm4::selfcolor = 0;
    }
    srand((unsigned int)time(0));
    uint64_t boardq = 0;
    uint64_t boardt = 0;
    for (i = 0; i < 64; i++) {
        if (atoi(argv[i + 2]) == 2) {
            boardq |= (uint64_t(1) << i);
        }else if (atoi(argv[i + 2]) == 1){
            boardq |= (uint64_t(1) << i);
            boardt |= (uint64_t(1) << i);
        }else if (atoi(argv[i + 2]) == -1){
            boardt |= (uint64_t(1) << i);
        }else{
            
        }
    }
    mctsmm4::Node* root = new mctsmm4::Node(boardq, boardt, NULL, !mctsmm4::selfcolor);
    mctsmm4::Node* child = mctsmm4::MonteCarlo(root);
    int a = 0;
    int b = 0;
    int c = 0;
    
    if (child == NULL) {
        printf("-2");
        return 0;
    }
    
    uint64_t cboardq = child->boardq;
    uint64_t cboardt = child->boardt;
    
    uint64_t y = 1;
    uint64_t condition = boardq & (~cboardq);
    while (y) {
        if (y & condition) {
            break;
        }
        a++;
        y <<= 1;
    }
    
    condition = (~boardq) & cboardq;
    y = 1;
    while (y) {
        if (y & condition) {
            break;
        }
        b++;
        y <<= 1;
    }
    
    condition = (~cboardq) & cboardt & (boardq | (~boardt));
    y = 1;
    while (y) {
        if (y & condition) {
            break;
        }
        c++;
        y <<= 1;
    }
    
    printf("%d %d %d", a, b, c);
    
    return 0;
}


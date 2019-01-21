//
//  ai.h
//  ai-final
//
//  Created by 虞晨曦 on 2019/1/21.
//  Copyright © 2019年 虞晨曦. All rights reserved.
//

#ifndef ai_h
#define ai_h

#include <iostream>
#include <math.h>
#include <time.h>
#include <stdlib.h>
#include <stdint.h>
#include <algorithm>
#define EPSILON (1.0e-5)
namespace mctsmm4{
using namespace std;
double ttime = 0.0;
const double c = 0.35;
const int max_depth = 0;
int max_iteration = 80000;
int max_children = 1200;
int max_steps = 8;
const int expansion_limit = 20;
int selfcolor = 1;

uint64_t* queenmove = new uint64_t[28];
uint64_t* setbarrier = new uint64_t[28];

typedef struct strsteps{
    uint64_t boardq;
    uint64_t boardt;
    double score;
} steps;

steps* psteps = (steps*)malloc(2500 * sizeof(steps));

int irand(int n){
    return rand() % n;
}

uint64_t* validMoves(uint64_t position, uint64_t boardq, uint64_t boardt, int type){
    uint64_t k = uint64_t(1);
    uint64_t* moves;
    if (type) {
        moves = setbarrier;
    }else{
        moves = queenmove;
    }
    uint64_t y = position;
    while (y >> 8) {
        y >>= 8;
    }
    uint64_t mod = 0;
    while (y >> 1){
        y >>= 1;
        mod++;
    }
    
    uint64_t board0 = boardq | boardt;
    
    uint64_t x = position;
    uint64_t num = uint64_t(0);
    
    while (x && num < mod){
        x >>= 9;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
            num++;
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < mod){
        x <<= 7;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
            num++;
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < 7 - mod){
        x >>= 7;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
            num++;
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < 7 - mod){
        x <<= 9;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
            num++;
        }
    }
    
    x = position;
    while (x){
        x >>= 8;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
        }
    }
    
    x = position;
    while (x){
        x <<= 8;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < 7 - mod){
        x <<= 1;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
            num++;
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < mod){
        x >>= 1;
        if (x & board0){
            break;
        }else if (x){
            moves[k] = x;
            k++;
            num++;
        }
    }
    
    moves[0] = k - 1;
    
    return moves;
}

int nextStep(uint64_t& boardq, uint64_t& boardt, bool curcolor){
    
    int i = 0;
    uint64_t queen[4];
    int k = 0;
    uint64_t y = uint64_t(1);
    
    if (curcolor) {
        while (y){
            if ((boardq & y) && (boardt & y)){
                queen[k] = y;
                k++;
            }
            y <<= 1;
        }
    }else{
        while (y){
            if ((boardq & y) && !(boardt & y)){
                queen[k] = y;
                k++;
            }
            y <<= 1;
        }
    }
    
    int j = 0;
    uint64_t curboardq;
    uint64_t curboardt;
    for (i = 4; i >= 1; i--){
        int random = irand(i);// choose queen
        uint64_t position = queen[random];// choose queen end
        
        uint64_t* queenmove = validMoves(position, boardq, boardt, 0);// queen move
        if (!queenmove[0]){
            for (j = random; j < i - 1; j++){
                queen[j] = queen[j + 1];
            }
            if (i == 1){
                return 0;
            }
            continue;
        }
        curboardq = boardq & (~position);
        curboardt = boardt & (~position);
        random = irand((int)queenmove[0]) + 1;
        curboardq |= queenmove[random];
        if (curcolor){
            curboardt |= queenmove[random];
        }
        
        uint64_t* setbarrier = validMoves(queenmove[random], curboardq, curboardt, 1);// set block
        random = irand((int)setbarrier[0]) + 1;
        curboardq &= ~setbarrier[random];
        curboardt |= setbarrier[random];
        break;
    }
    boardq = curboardq;
    boardt = curboardt;
    return 1;
}

int totalmoves(uint64_t boardq, uint64_t boardt, int color){
    int i = 0;
    int k = 0;
    uint64_t y = uint64_t(1);
    uint64_t queen[4];
    if (color) {
        while (y){
            if ((boardq & y) && (boardt & y)){
                queen[k] = y;
                k++;
            }
            y <<= 1;
        }
    }else{
        while (y){
            if ((boardq & y) && !(boardt & y)){
                queen[k] = y;
                k++;
            }
            y <<= 1;
        }
    }
    int total = 0;
    for (i = 0; i < 4; i++){
        uint64_t* moves = validMoves(queen[i], boardq, boardt, 0);
        total += moves[0];
    }
    return total;
}

uint64_t forbidden[4];
uint64_t zone[4];
void calZone(uint64_t position, int depth, int queenNum){
    if (!depth) {
        return;
    }
    uint64_t y = position;
    while (y >> 8) {
        y >>= 8;
    }
    uint64_t mod = 0;
    while (y >> 1){
        y >>= 1;
        mod++;
    }
    
    uint64_t x = position;
    uint64_t num = uint64_t(0);
    
    while (x && num < mod){
        x >>= 9;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            num++;
            calZone(x, depth - 1, queenNum);
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < mod){
        x <<= 7;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            num++;
            calZone(x, depth - 1, queenNum);
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < 7 - mod){
        x >>= 7;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            num++;
            calZone(x, depth - 1, queenNum);
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < 7 - mod){
        x <<= 9;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            num++;
            calZone(x, depth - 1, queenNum);
        }
    }
    
    x = position;
    while (x){
        x >>= 8;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            calZone(x, depth - 1, queenNum);
        }
    }
    
    x = position;
    while (x){
        x <<= 8;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            calZone(x, depth - 1, queenNum);
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < 7 - mod){
        x <<= 1;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            num++;
            calZone(x, depth - 1, queenNum);
        }
    }
    
    x = position;
    num = uint64_t(0);
    while (x && num < mod){
        x >>= 1;
        if (x & forbidden[queenNum]){
            break;
        }else if (x){
            zone[queenNum] |= x;
            forbidden[queenNum] |= x;
            num++;
            calZone(x, depth - 1, queenNum);
        }
    }
    return;
}

uint64_t evaluate(uint64_t boardq, uint64_t boardt, bool color, int depth){
    uint64_t y = uint64_t(1);
    uint64_t queen[4];
    int k = 0;
    if (color) {
        while (y){
            if ((boardq & y) && (boardt & y)){
                queen[k] = y;
                k++;
            }
            y <<= 1;
        }
    }else{
        while (y){
            if ((boardq & y) && !(boardt & y)){
                queen[k] = y;
                k++;
            }
            y <<= 1;
        }
    }
    
    int i = 0;
    for (i = 0; i < 4; i++) {
        forbidden[i] = boardq | boardt;
        zone[i] = queen[i];
        calZone(queen[i], depth, i);
    }
    y = zone[0] | zone[1] | zone[2] | zone[3];
    return y;
}

double evaluatefunc(uint64_t boardq, uint64_t boardt, bool color){
    uint64_t me = evaluate(boardq, boardt, selfcolor, 2);
    uint64_t him = evaluate(boardq, boardt, !selfcolor, 2);
    uint64_t y = me;
    int i = 0;
    while (y) {
        if (y & uint64_t(1)) {
            i++;
        }
        y >>= 1;
    }
    y = him;
    int j = 0;
    while (y) {
        if (y & uint64_t(1)) {
            j++;
        }
        y >>= 1;
    }
    y = me & him;
    int k = 0;
    while (y) {
        if (y & uint64_t(1)) {
            k++;
        }
        y >>= 1;
    }
    
    uint64_t me1 = evaluate(boardq, boardt, selfcolor, 1);
    uint64_t him1 = evaluate(boardq, boardt, !selfcolor, 1);
    y = me1;
    int i1 = 0;
    while (y) {
        if (y & uint64_t(1)) {
            i1++;
        }
        y >>= 1;
    }
    y = him1;
    int j1 = 0;
    while (y) {
        if (y & uint64_t(1)) {
            j1++;
        }
        y >>= 1;
    }
    
    if (selfcolor == color) {
        return (i - 0.4 * k + double(i1 - j1) / 4.0) / double(i + j - k);
    }else{
        return (i - 0.6 * k + double(i1 - j1) / 4.0) / double(i + j - k);
    }
}

bool cmp(steps& a, steps& b){
    if (a.score < b.score) {
        return true;
    }else{
        return false;
    }
}

class Node{
public:
    double w;
    double n;
    bool color;
    
    uint64_t boardq;
    uint64_t boardt;
    
    int numChildren;
    Node** Children;
    
    Node* parent;
    
    Node(uint64_t boardq, uint64_t boardt, Node* parent, bool color){
        this->boardq = boardq;
        this->boardt = boardt;
        this->parent = parent;
        this->color = color;
        this->w = EPSILON;
        this->n = EPSILON;
        this->numChildren = 0;
        this->Children = NULL;
    }
    
    double score(){
        return w / n + c * sqrt(log(parent->n + 1.0) / n);
    }
    
    Node* genChildren(){
        if (this->numChildren != 0){
            int childselected = irand(numChildren);
            return this->Children[childselected];
        }
        bool curcolor = !color;
        int i = 0;
        uint64_t queen[4];
        int k = 0;
        uint64_t y = uint64_t(1);
        if (color) {
            while (y){
                if ((boardq & y) && !(boardt & y)){
                    queen[k] = y;
                    k++;
                }
                y <<= 1;
            }
        }else{
            while (y){
                if ((boardq & y) && (boardt & y)){
                    queen[k] = y;
                    k++;
                }
                y <<= 1;
            }
        }
        
        
        uint64_t j = uint64_t(0);
        uint64_t t = uint64_t(0);
        int len = 0;
        
        for (i = 0; i < 4; i++){
            uint64_t* queenmove = validMoves(queen[i], boardq, boardt, 0);
            uint64_t curboardq = boardq ^ queen[i];
            uint64_t curboardt = (boardt | queen[i]) ^ queen[i];
            
            for (j = 1; j <= queenmove[0]; j++){
                
                curboardq ^= queenmove[j];
                if (curcolor){
                    curboardt |= queenmove[j];
                }
                
                uint64_t* setbarrier = validMoves(queenmove[j], curboardq, curboardt, 1);
                for (t = 1; t <= setbarrier[0]; t++){
                    
                    if (len >= max_children){
                        if (len == max_children) {
                            for (k = 0; k < len; k++) {
                                psteps[k].score = evaluatefunc(psteps[k].boardq, psteps[k].boardt, this->color);
                            }
                            make_heap(psteps, psteps + len, cmp);
                            psteps[len].boardq = curboardq;
                            psteps[len].boardt = curboardt | setbarrier[t];
                            psteps[len].score = evaluatefunc(psteps[len].boardq, psteps[len].boardt, this->color);
                            push_heap(psteps, psteps + len + 1, cmp);
                        }else{
                            psteps[len].boardq = curboardq;
                            psteps[len].boardt = curboardt | setbarrier[t];
                            psteps[len].score = evaluatefunc(psteps[len].boardq, psteps[len].boardt, this->color);
                            push_heap(psteps, psteps + len + 1, cmp);
                        }
                    }else{
                        psteps[len].boardq = curboardq;
                        psteps[len].boardt = curboardt | setbarrier[t];
                    }
                    len++;
                }
                curboardq ^= queenmove[j];
                (curboardt |= queenmove[j]) ^= queenmove[j];
            }
        }
        
        len = (len >= max_children)? max_children : len;
        numChildren = len;
        if (len == 0){
            return NULL;
        }else{
            Children = new Node*[len];
            for (i = 0; i < len; i++){
                uint64_t resboardq = psteps[i].boardq;
                uint64_t resboardt = psteps[i].boardt;
                Children[i] = new Node(resboardq, resboardt, this, curcolor);
            }
            
            int childselected = irand(len);
            return Children[childselected];
        }
        
    }
};

Node* MonteCarlo(Node* root){
    clock_t start = clock();
    clock_t finish;
    int i = 0;
    for (i = 0; i < max_iteration; i++){
        int depth = max_depth;
        Node* pnode = root;
        while (pnode->numChildren != 0){
            Node* pmax = pnode->Children[0];
            int j = 0;
            double max = -10000;
            double cmax = -10000;
            for (j = 0; j < pnode->numChildren; j++){
                cmax = pnode->Children[j]->score();
                if (max < cmax){
                    max = cmax;
                    pmax = pnode->Children[j];
                }
            }
            pnode = pmax;
            depth--;
        }
        
        Node* playout = NULL;
        if ((depth >= 0 && pnode->n > expansion_limit) || pnode == root){
            playout = pnode->genChildren();
        }else{
            playout = pnode;
        }
        
        double value;
        if (playout == NULL){
            playout = pnode;
            if (pnode->color == selfcolor){
                value = 1.0;
            }else{
                value = 0.0;
            }
        }else{
            uint64_t cboardq = playout->boardq;
            uint64_t cboardt = playout->boardt;
            
            bool curcolor = playout->color;
            int m = 0;
            bool states = 1;
            while (m < max_steps && states){
                curcolor = !curcolor;
                states = nextStep(cboardq, cboardt, curcolor);
                m++;
            }
            if (!states){
                if (!curcolor == selfcolor){
                    value = 1.0;
                }else{
                    value = 0.0;
                }
            }else{
                value = evaluatefunc(cboardq, cboardt, !curcolor);
            }
        }
        
        /*
         * Backpropagation
         */
        pnode = playout;
        while (pnode != NULL){
            pnode->n += 1.0;
            pnode->w += value;
            pnode = pnode->parent;
        }
        if (i % 250) {
            
        }else{
            finish = clock();
            double ctime = (double)(finish - start) / CLOCKS_PER_SEC;
            if (ctime >= 0.95) {
                break;
            }
        }
    }
    
    int k = 0;
    Node* child = NULL;
    double max = -10000.0;
    double curmax = -10000.0;
    double nmax = 0.0;
    Node* reschild = NULL;
    for (k = 0; k < root->numChildren; k++){
        child = root->Children[k];
        curmax = (child->w) / (child->n);
        if (max < curmax){
            max = curmax;
            reschild = child;
        }else if (max == curmax && nmax < child->n){
            reschild = child;
        }else{
            
        }
    }
    
    return reschild;
}

}

#endif /* ai_h */

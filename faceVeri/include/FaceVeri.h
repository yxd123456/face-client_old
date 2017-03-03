#ifndef __FACEVERI_H__
#define __FACEVERI_H__

#define FEAT_DIM 256

struct FACERC
{
	int x, y, width, height;
};

void faceVeriInit(char* faceVeriConfigPath);
int faceDetectCamera(unsigned char* pFrame, FACERC& rc, int w, int h);
int faceFeatureExtractCamera(char* imgPath, FACERC& rc, float** featc, int isCompare);
int faceFeatureExtractIDCard(char* imgPath, FACERC& rc, float** feati);
int faceFeatureCompare(float* score);
void faceVeriFree();
void debugTest(unsigned char* pFrame, int w, int h, char* imagepath);
#endif // __FACEVERI_H__
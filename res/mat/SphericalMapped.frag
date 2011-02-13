#import "Common/ShaderLib/Texture.glsllib"

#define PI 3.141592654
#define FOURPI 12.5663706

 
varying vec2 texCoord;
 
uniform sampler2D m_ColorMap;
 
uniform float m_AlphaMultiplier;
uniform float m_TextureOffset;
uniform float m_TexturePortion;
 
void main(){
	vec2 loc = vec2(texCoord.x * 2.0 - 1.0, texCoord.y * 2.0 - 1.0);
	float d = loc.x * loc.x +  loc.y * loc.y;
    if(d < 1.0) {
    	float fakeZ = sqrt(1.0 - loc.x*loc.x - loc.y*loc.y);
		float fakeV = acos(loc.y);
		float fakeU = 0.0;
		if(fakeZ >= 0.0) {
			fakeU = acos(loc.x/sin(fakeV)) / FOURPI + m_TextureOffset;
		} else {
			fakeU = (PI + acos(loc.x/sin(fakeV))) / FOURPI + m_TextureOffset;
		}
		if(fakeU >= 1.0) {
			fakeU = fakeU - 1.0;
		}
		gl_FragColor = Texture_GetColor(m_ColorMap, vec2(fakeU * m_TexturePortion, fakeV * m_TexturePortion / PI));
    	
	} else {
		gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
	}
    #ifdef USE_ALPHA_MULTIPLIER
        gl_FragColor.a *= m_AlphaMultiplier;
    #endif
}
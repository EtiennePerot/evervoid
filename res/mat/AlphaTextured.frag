#import "Common/ShaderLib/Texture.glsllib"
 
varying vec2 texCoord;
 
uniform sampler2D m_ColorMap;
 
uniform float m_AlphaMultiplier;
uniform vec4 m_HueColor;
uniform float m_HueMultiplier;
 
void main(){
    #ifdef NORMAL_LATC
        vec3 newNorm = vec3(texture2D(m_ColorMap, texCoord).ag, 0.0);
        newNorm = Common_UnpackNormal(newNorm);
        newNorm.b = sqrt(1.0 - (newNorm.x * newNorm.x) - (newNorm.y * newNorm.y));
        newNorm = Common_PackNormal(newNorm);
        gl_FragColor = vec4(newNorm, 1.0);
    #elif defined(SHOW_ALPHA)
        gl_FragColor = vec4(texture2D(m_ColorMap, texCoord).a);
    #else
        gl_FragColor = Texture_GetColor(m_ColorMap, texCoord);
    #endif
    #ifdef USE_ALPHA_MULTIPLIER
        gl_FragColor.a *= m_AlphaMultiplier;
    #endif
    #ifdef USE_HUE_COLOR
    	gl_FragColor.r *= m_HueColor.r*m_HueMultiplier;
    	gl_FragColor.g *= m_HueColor.g*m_HueMultiplier;
    	gl_FragColor.b *= m_HueColor.b*m_HueMultiplier;
    	gl_FragColor.a *= m_HueColor.a*m_HueMultiplier;
    #endif
    #ifdef NORMALIZE
        gl_FragColor = vec4(normalize(gl_FragColor.xyz), gl_FragColor.a);
    #endif
}
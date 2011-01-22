#import "Common/ShaderLib/Texture.glsllib"
 
varying vec2 texCoord;
 
uniform sampler2D m_AlphaMap;
 
uniform float m_AlphaMultiplier;
uniform vec4 m_ShadeColor;
uniform float m_ShadeAngle;
uniform float m_ShadePortion;
uniform float m_ShadeGradientPortion;

const float PI=3.1415926535;
 
void main(){
    gl_FragColor = vec4(m_ShadeColor.r, m_ShadeColor.g, m_ShadeColor.b, m_ShadeColor.a * Texture_GetColor(m_AlphaMap, texCoord).a);
    // Compute center-based coords (origin = (0, 0) and diameter = 2 units)
    vec2 centerCoords = vec2(texCoord.x * 2.0 - 1.0, texCoord.y * 2.0 - 1.0);
    // Then compute rotated vector
    vec2 centerRotated = vec2(centerCoords.x * cos(m_ShadeAngle) - centerCoords.y * sin(m_ShadeAngle),
                              centerCoords.x * sin(m_ShadeAngle) + centerCoords.y * cos(m_ShadeAngle));
    // Finally, take x component of un-normalized vector
    float shade = 1.0 - (centerRotated.x + 1.0) / 2.0;
    if(shade > m_ShadePortion){
    	gl_FragColor = vec4(0, 0, 0, 0);
    }
    else{
    	if(shade > m_ShadePortion - m_ShadeGradientPortion){
    		gl_FragColor = vec4(gl_FragColor.r, gl_FragColor.g, gl_FragColor.b, gl_FragColor.a * (1.0 - (shade - m_ShadePortion + m_ShadeGradientPortion) / m_ShadeGradientPortion));
    	}
		#ifdef USE_ALPHA_MULTIPLIER
			gl_FragColor = vec4(gl_FragColor.r, gl_FragColor.g, gl_FragColor.b, gl_FragColor.a * m_AlphaMultiplier);
		#endif
	}
}